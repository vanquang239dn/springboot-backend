
package vn.vanquang239dn.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import vn.vanquang239dn.dto.request.UserCreationRequest;
import vn.vanquang239dn.dto.request.UserPasswordUpdateRequest;
import vn.vanquang239dn.dto.request.UserUpdateRequest;
import vn.vanquang239dn.dto.response.UserResponse;
import vn.vanquang239dn.model.enums.Gender;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/mock/user")
@Tag(name = "Mock Controller", description = "Controller for managing users")
public class MockUserController {

    // Mock API for fetching user list
    @Operation(summary = "Get user list", description = "Mock API : Returns a list of users")
    @GetMapping("/list")
    public Map<String, Object> getUserList(@RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        // Implementation for fetching user list based on keyword
        List<UserResponse> UserList = List.of(
                new UserResponse(1L, "John", "Doe", Gender.MALE, LocalDate.now(),
                        "JohnDoe@gmail.com", "123-456-7890", "admin"),
                new UserResponse(2L, "Jane", "Smith", Gender.FEMALE, LocalDate.now(),
                        "JaneSmith@gmail.com", "098-765-4321", "user"));

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("message", "user list");
        response.put("data", UserList);

        return response;
    }

    // Mock API for fetching user details
    @Operation(summary = "Get user details", description = "Mock API : Returns details of a specific user")
    @GetMapping("/{userId}")
    public Map<String, Object> getUserDetail(@PathVariable Long userId) {

        // Implementation for fetching user details
        UserResponse userResponse = new UserResponse(userId, "John", "Doe", Gender.MALE, LocalDate.now(),
                "JohnDoe@gmail.com", "123-456-7890", "admin");

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("message", "user detail");
        response.put("data", userResponse);

        return response;
    }

    // Mock API for creating a new user
    @Operation(summary = "Create user", description = "Mock API : Create a new user")
    @PostMapping("/add")
    public Map<String, Object> createUser(@RequestBody UserCreationRequest userRequest) {

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.CREATED.value());
        response.put("message", "user created");
        response.put("data", 3L);

        return response;
    }

    // Mock API for updating user details
    @Operation(summary = "Update user", description = "Mock API : Update existing user details")
    @PutMapping("/update")
    public Map<String, Object> updateUser(@RequestBody UserUpdateRequest userRequest) {

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.ACCEPTED.value());
        response.put("message", "user updated");
        response.put("data", "");

        return response;
    }

    // Mock API for updating user password
    @Operation(summary = "Update user password", description = "Mock API : Update existing user password")
    @PatchMapping("/update-pwd")
    public Map<String, Object> updatePassword(@RequestBody UserPasswordUpdateRequest userRequest) {

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.ACCEPTED.value());
        response.put("message", "passoword updated");
        response.put("data", "");

        return response;
    }

    // Mock API for delete user
    @Operation(summary = "Delete user", description = "Mock API : Delete an existing user")
    @DeleteMapping("/del/{userId}")
    public Map<String, Object> deleteUser(@PathVariable Long userId) {

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.RESET_CONTENT.value());
        response.put("message", "user deleted");
        response.put("data", "");

        return response;
    }
}
