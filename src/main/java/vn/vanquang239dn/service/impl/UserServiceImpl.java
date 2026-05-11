package vn.vanquang239dn.service.impl;

import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.vanquang239dn.dto.request.UserCreationRequest;
import vn.vanquang239dn.dto.request.UserPasswordUpdateRequest;
import vn.vanquang239dn.dto.request.UserUpdateRequest;
import vn.vanquang239dn.dto.response.UserPageResponse;
import vn.vanquang239dn.dto.response.UserResponse;
import vn.vanquang239dn.exception.DuplicateResourceException;
import vn.vanquang239dn.exception.ResourceNotFoundException;
import vn.vanquang239dn.model.entity.AddressEntity;
import vn.vanquang239dn.model.entity.UserEntity;
import vn.vanquang239dn.model.enums.UserStatus;
import vn.vanquang239dn.repository.AddressRepository;
import vn.vanquang239dn.repository.UserRepository;
import vn.vanquang239dn.service.UserService;

@Service
@Slf4j(topic = "USER-SERVICE")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final PasswordEncoder passwordEncoder;

    // Define allowed sort fields for validation
    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of(
            "id",
            "firstName",
            "lastName",
            "email",
            "phone");

    @Override
    public UserPageResponse findAll(String keyword, String sortBy, int page, int size) {
        // Implement logic to fetch all users from the database
        log.info("Fetching user list with keyword={}, sortBy={}, page={}, size={}", keyword, sortBy, page, size);

        // Default sort
        Sort.Order sortOrder = new Sort.Order(Sort.Direction.ASC, "id");

        // Handle sort
        if (StringUtils.hasLength(sortBy)) {

            // Validate sortBy format (e.g., "field:direction")
            Pattern sortByPattern = Pattern.compile("(\\w+):(asc|desc)", Pattern.CASE_INSENSITIVE);

            Matcher matcher = sortByPattern.matcher(sortBy);

            if (matcher.matches()) {

                // Extract sort field and sort direction from sortBy
                String sortField = matcher.group(1);
                String sortDirection = matcher.group(2);

                // Validate sort field against allowed fields
                if (!ALLOWED_SORT_FIELDS.contains(sortField)) {
                    throw new IllegalArgumentException("Invalid sort field");
                }

                // Create Sort. Order based on direction
                sortOrder = new Sort.Order(Sort.Direction.fromString(sortDirection), sortField);

            } else {
                log.error("Invalid sortBy format: {}", sortBy);
            }
        }

        int pageNo = 0;
        if (page > 0) {
            pageNo = page - 1;
        }

        // Paging
        Pageable pageable = PageRequest.of(pageNo, size, Sort.by(sortOrder));

        // Declare
        Page<UserEntity> userEntities;

        // Search
        if (StringUtils.hasText(keyword)) {
            userEntities = userRepository.searchByKeyword(keyword, pageable);
        } else {
            userEntities = userRepository.findAll(pageable);
        }

        return UserPageResponse.builder()
                .pageNumber(pageNo)
                .pageSize(size)
                .totalElements(userEntities.getTotalElements())
                .totalPages(userEntities.getTotalPages())
                .listUserResponse(userEntities.getContent().stream()
                        .map(userEntity -> UserResponse.builder()
                                .id(userEntity.getId())
                                .lastName(userEntity.getLastName())
                                .firstName(userEntity.getFirstName())
                                .gender(userEntity.getGender())
                                .birthday(userEntity.getBirthday())
                                .email(userEntity.getEmail())
                                .phone(userEntity.getPhone())
                                .username(userEntity.getUsername())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public UserResponse findById(Long userId) {
        // Implement logic to fetch a user by ID from the database
        log.info("Fetching user details for ID={}", userId);

        UserEntity userEntity = getUserEntityById(userId);

        return UserResponse.builder()
                .id(userEntity.getId())
                .lastName(userEntity.getLastName())
                .firstName(userEntity.getFirstName())
                .gender(userEntity.getGender())
                .birthday(userEntity.getBirthday())
                .email(userEntity.getEmail())
                .phone(userEntity.getPhone())
                .username(userEntity.getUsername())
                .build();
    }

    @Override
    public UserResponse findByUsername(String username) {
        // Implement logic to fetch a user by username from the database
        return null;
    }

    @Override
    public UserResponse findByEmail(String email) {
        // Implement logic to fetch a user by email from the database
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(UserCreationRequest req) {
        // Implement logic to save a new user to the database
        log.info("Creating user with username={}", req.getUsername());

        UserEntity userEntity = new UserEntity();
        userEntity.setFirstName(req.getFirstName());
        userEntity.setLastName(req.getLastName());
        userEntity.setGender(req.getGender());
        userEntity.setBirthday(req.getBirthday());
        userEntity.setEmail(req.getEmail());
        userEntity.setPhone(req.getPhone());
        userEntity.setUsername(req.getUsername());
        userEntity.setPassword("123456");
        userEntity.setType(req.getType());
        userEntity.setStatus(UserStatus.NONE);

        userRepository.save(userEntity);

        if (userEntity.getId() != null) {
            List<AddressEntity> addresses = req.getAddresses().stream()
                    .map(addressReq -> {
                        AddressEntity addressEntity = new AddressEntity();
                        addressEntity.setApartmentNumber(addressReq.getApartmentNumber());
                        addressEntity.setFloor(addressReq.getFloor());
                        addressEntity.setBuilding(addressReq.getBuilding());
                        addressEntity.setStreetNumber(addressReq.getStreetNumber());
                        addressEntity.setStreet(addressReq.getStreet());
                        addressEntity.setCity(addressReq.getCity());
                        addressEntity.setCountry(addressReq.getCountry());
                        addressEntity.setAddressType(addressReq.getAddressType());
                        addressEntity.setUserId(userEntity.getId());
                        return addressEntity;
                    })
                    .toList();
            addressRepository.saveAll(addresses);
        } else {
            log.error("Failed to create user with username={}", req.getUsername());
            throw new RuntimeException("Failed to create user");
        }

        log.info("User created with ID={}", userEntity.getId());

        return userEntity.getId();

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long update(UserUpdateRequest req) {
        // Implement logic to update an existing user in the database

        // Check duplicate email
        boolean emailExists = userRepository.existsByEmailAndIdNot(req.getEmail(), req.getUserId());

        if (emailExists) {
            throw new DuplicateResourceException("email", req.getEmail(), "Email already exists: " + req.getEmail());
        }

        // Get user entity by ID, if not found throw ResourceNotFoundException
        UserEntity userEntity = getUserEntityById(req.getUserId());
        userEntity.setFirstName(req.getFirstName());
        userEntity.setLastName(req.getLastName());
        userEntity.setGender(req.getGender());
        userEntity.setBirthday(req.getBirthday());
        userEntity.setEmail(req.getEmail());
        userEntity.setPhone(req.getPhone());
        userEntity.setUsername(req.getUsername());
        userRepository.save(userEntity);

        log.info("User updated with ID={}", userEntity.getId());

        // Save addresses if provided
        if (req.getAddresses() != null) {
            List<AddressEntity> addresses = req.getAddresses().stream()
                    .map(addressReq -> {
                        AddressEntity addressEntity = addressRepository.findByUserIdAndAddressType(userEntity.getId(),
                                addressReq.getAddressType());
                        if (addressEntity == null) {
                            addressEntity = new AddressEntity();
                            addressEntity.setApartmentNumber(addressReq.getApartmentNumber());
                            addressEntity.setFloor(addressReq.getFloor());
                            addressEntity.setBuilding(addressReq.getBuilding());
                            addressEntity.setStreetNumber(addressReq.getStreetNumber());
                            addressEntity.setStreet(addressReq.getStreet());
                            addressEntity.setCity(addressReq.getCity());
                            addressEntity.setCountry(addressReq.getCountry());
                            addressEntity.setAddressType(addressReq.getAddressType());
                            addressEntity.setUserId(userEntity.getId());
                        } else {
                            addressEntity.setApartmentNumber(addressReq.getApartmentNumber());
                            addressEntity.setFloor(addressReq.getFloor());
                            addressEntity.setBuilding(addressReq.getBuilding());
                            addressEntity.setStreetNumber(addressReq.getStreetNumber());
                            addressEntity.setStreet(addressReq.getStreet());
                            addressEntity.setCity(addressReq.getCity());
                            addressEntity.setCountry(addressReq.getCountry());
                        }
                        return addressEntity;
                    })
                    .toList();

            // Save all addresses (both new and updated)
            addressRepository.saveAll(addresses);
        }

        return userEntity.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePassword(UserPasswordUpdateRequest req) {
        // Implement logic to update a user's password in the database
        log.info("Updating password for user ID={}", req.getUserId());

        // Get user entity by ID, if not found throw ResourceNotFoundException
        UserEntity userEntity = getUserEntityById(req.getUserId());
        if (req.getNewPassword().equals(req.getConfirmPassword())) {
            userEntity.setPassword(passwordEncoder.encode(req.getNewPassword()));
        } else {
            log.error("New password and confirm password do not match for user ID={}", req.getUserId());
            throw new IllegalArgumentException("New password and confirm password do not match");
        }

        userRepository.save(userEntity);
        log.info("Password updated for user ID={}", userEntity.getId());
    }

    @Override
    public void deleteById(Long userId) {
        // Implement logic to delete a user by ID from the database
        log.info("Deleting user with ID={}", userId);

        UserEntity userEntity = getUserEntityById(userId);
        userEntity.setStatus(UserStatus.INACTIVE);
        userRepository.save(userEntity);
        log.info("User deleted with ID={}", userId);
    }

    private UserEntity getUserEntityById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("userId", userId.toString(),
                        "User not found with Id: " + userId));
    }

}
