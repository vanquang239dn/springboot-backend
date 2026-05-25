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
import vn.vanquang239dn.dto.response.UserPageResponse;
import vn.vanquang239dn.dto.response.UserResponse;
import vn.vanquang239dn.service.impl.UserServiceImpl;

import java.util.LinkedHashMap;
import java.util.Map;

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
    public Map<String, Object> getUserList(@RequestParam(required = false) String keyword,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        log.info("Fetch user detail list");

        // Implementation for fetching user list based on keyword
        UserPageResponse userPageResponse = userService.findAll(keyword, sortBy, page, size);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("message", "user list");
        response.put("data", userPageResponse);

        return response;
    }

    // API for fetching user details
    @Operation(summary = "Get user details", description = "Returns details of a specific user")
    @GetMapping("/{userId}")
    @PreAuthorize("hasAuthority('USER_READ')")
    public Map<String, Object> getUserDetail(@PathVariable Long userId) {

        log.info("Fetch user detail with id={}", userId);

        // Find user by Id
        UserResponse userResponse = userService.findById(userId);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("message", "user detail");
        response.put("data", userResponse);

        return response;
    }

    // API for creating a new user
    @Operation(summary = "Create user", description = "Create a new user")
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('USER_ADD')")
    public Map<String, Object> createUser(@RequestBody @Valid UserCreationRequest userRequest) {

        log.info("Add new user");

        // Add new user
        userService.save(userRequest);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.CREATED.value());
        response.put("message", "user created");

        return response;
    }

    // API for updating user details
    @Operation(summary = "Update user", description = "Update existing user details")
    @PutMapping("/update")
    @PreAuthorize("hasAuthority('USER_UPDATE')")
    public Map<String, Object> updateUser(@RequestBody @Valid UserUpdateRequest userRequest) {

        log.info("Updating user with id={}", userRequest.getUserId());

        // Update user
        userService.update(userRequest);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.ACCEPTED.value());
        response.put("message", "user updated");

        return response;
    }

    // API for updating user password
    @Operation(summary = "Update user password", description = "Update existing user password")
    @PatchMapping("/update-pwd")
    @PreAuthorize("hasAuthority('USER_UPDATE')")
    public Map<String, Object> updatePassword(@RequestBody UserPasswordUpdateRequest userRequest) {

        userService.updatePassword(userRequest);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.ACCEPTED.value());
        response.put("message", "password updated");

        return response;
    }

    // API for delete use
    @Operation(summary = "Delete user", description = "Delete an existing user")
    @DeleteMapping("/del/{userId}")
    @PreAuthorize("hasAuthority('USER_DELETE')")
    public Map<String, Object> deleteUser(@PathVariable Long userId) {

        userService.deleteById(userId);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.RESET_CONTENT.value());
        response.put("message", "user deleted");
        response.put("data", "");

        return response;
    }
}
