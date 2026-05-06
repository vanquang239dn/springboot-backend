package vn.vanquang239dn.service;

import java.util.List;

import vn.vanquang239dn.controller.request.UserCreationRequest;
import vn.vanquang239dn.controller.request.UserPasswordUpdateRequest;
import vn.vanquang239dn.controller.request.UserUpdateRequest;
import vn.vanquang239dn.controller.response.UserResponse;

public interface UserService {

    List<UserResponse> findAll();

    UserResponse findById(Long userId);

    UserResponse findByUsername(String username);

    UserResponse findByEmail(String email);

    Long save(UserCreationRequest req);

    Long update(UserUpdateRequest req);

    void updatePassword(UserPasswordUpdateRequest req);

    void deleteById(Long userId);

}
