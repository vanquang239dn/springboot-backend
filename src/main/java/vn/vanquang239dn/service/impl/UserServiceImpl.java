package vn.vanquang239dn.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.vanquang239dn.controller.request.UserCreationRequest;
import vn.vanquang239dn.controller.request.UserPasswordUpdateRequest;
import vn.vanquang239dn.controller.request.UserUpdateRequest;
import vn.vanquang239dn.controller.response.UserResponse;
import vn.vanquang239dn.model.UserEntity;
import vn.vanquang239dn.model.enums.UserStatus;
import vn.vanquang239dn.repository.UserRepository;
import vn.vanquang239dn.service.UserService;

@Service
@Slf4j(topic = "USER-SERVICE")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<UserResponse> findAll() {
        // Implement logic to fetch all users from the database
        return List.of();
    }

    @Override
    public UserResponse findById(Long userId) {
        // Implement logic to fetch a user by ID from the database
        return null;
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
        userEntity.setType(req.getType());
        userEntity.setStatus(UserStatus.NONE);

        userRepository.save(userEntity);
        return userEntity.getId();

    }

    @Override
    public Long update(UserUpdateRequest req) {
        // Implement logic to update an existing user in the database

    }

    @Override
    public void updatePassword(UserPasswordUpdateRequest req) {
        // Implement logic to update a user's password in the database
    }

    @Override
    public void deleteById(Long userId) {
        // Implement logic to delete a user by ID from the database
    }

}
