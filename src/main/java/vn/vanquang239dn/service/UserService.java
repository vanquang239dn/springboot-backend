package vn.vanquang239dn.service;

import vn.vanquang239dn.dto.request.UserCreationRequest;
import vn.vanquang239dn.dto.request.UserPasswordUpdateRequest;
import vn.vanquang239dn.dto.request.UserUpdateRequest;
import vn.vanquang239dn.dto.response.UserPageResponse;
import vn.vanquang239dn.dto.response.UserResponse;

public interface UserService {

    UserPageResponse findAll(String keyword, String sortBy, int page, int size);

    UserResponse findById(Long userId);

    UserResponse findByUsername(String username);

    UserResponse findByEmail(String email);

    Long save(UserCreationRequest req);

    Long update(UserUpdateRequest req);

    void updatePassword(UserPasswordUpdateRequest req);

    void deleteById(Long userId);

}
