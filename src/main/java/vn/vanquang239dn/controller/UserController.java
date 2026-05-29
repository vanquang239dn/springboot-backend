package vn.vanquang239dn.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.vanquang239dn.dto.request.UserCreationRequest;
import vn.vanquang239dn.dto.request.UserPasswordUpdateRequest;
import vn.vanquang239dn.dto.request.UserUpdateRequest;
import vn.vanquang239dn.dto.response.RestApiResponse;
import vn.vanquang239dn.dto.response.UserPageResponse;
import vn.vanquang239dn.dto.response.UserResponse;
import vn.vanquang239dn.service.impl.UserServiceImpl;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j(topic = "USER-CONTROLLER")
@Tag(name = "User Controller", description = "Controller for managing users")
@Validated
public class UserController {

    private final UserServiceImpl userService;

    // API for fetching user list
    @Operation(summary = "Get user list", description = "Returns a list of users")
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('USER_READ')")
    public RestApiResponse getUserList(@RequestParam(required = false) String keyword,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        log.info("Fetch user detail list");

        // Fetching user list based on keyword
        UserPageResponse userPageResponse = userService.findAll(keyword, sortBy, page, size);

        return buildRestApiResponse(HttpStatus.OK, "User list", userPageResponse);
    }

    // API for fetching user details
    @Operation(summary = "Get user details", description = "Returns details of a specific user")
    @GetMapping("/{userId}")
    @PreAuthorize("hasAuthority('USER_READ')")
    public RestApiResponse getUserDetail(@PathVariable Long userId) {

        log.info("Fetch user detail with id={}", userId);

        // Find user by Id
        UserResponse userResponse = userService.findById(userId);

        return buildRestApiResponse(HttpStatus.OK, "User detail", userResponse);
    }

    // API for creating a new user
    @Operation(summary = "Create user", description = "Create a new user")
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('USER_ADD')")
    public RestApiResponse createUser(@RequestBody @Valid UserCreationRequest userRequest) {

        log.info("Add new user");

        // Add new user
        userService.save(userRequest);

        return buildRestApiResponse(HttpStatus.CREATED, "User created", null);
    }

    // API for updating user details
    @Operation(summary = "Update user", description = "Update existing user details")
    @PutMapping("/update")
    @PreAuthorize("hasAuthority('USER_UPDATE')")
    public RestApiResponse updateUser(@RequestBody @Valid UserUpdateRequest userRequest) {

        log.info("Updating user with id={}", userRequest.getUserId());

        // Update user detail
        userService.update(userRequest);

        return buildRestApiResponse(HttpStatus.ACCEPTED, "User updated", null);
    }

    // API for updating user password
    @Operation(summary = "Update user password", description = "Update existing user password")
    @PatchMapping("/update-pwd")
    @PreAuthorize("hasAuthority('USER_UPDATE')")
    public RestApiResponse updatePassword(@RequestBody UserPasswordUpdateRequest userRequest) {

        log.info("Updating user with id={}", userRequest.getUserId());

        // Update user password
        userService.updatePassword(userRequest);

        return buildRestApiResponse(HttpStatus.ACCEPTED, "Password updated", null);
    }

    // API for delete use
    @Operation(summary = "Delete user", description = "Delete an existing user")
    @DeleteMapping("/del/{userId}")
    @PreAuthorize("hasAuthority('USER_DELETE')")
    public RestApiResponse deleteUser(@PathVariable Long userId) {

        log.info("Deleting user with id={}", userId);

        // Delete user by Id
        userService.deleteById(userId);

        return buildRestApiResponse(HttpStatus.RESET_CONTENT, "User deleted", null);
    }

    private RestApiResponse buildRestApiResponse(HttpStatus status, String message, Object data) {

        return RestApiResponse.builder()
                .status(status.value())
                .message(message)
                .data(data)
                .build();
    }
}
