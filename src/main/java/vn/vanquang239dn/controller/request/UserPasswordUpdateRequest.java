package vn.vanquang239dn.controller.request;

import java.io.Serializable;

import lombok.Getter;

@Getter
public class UserPasswordUpdateRequest implements Serializable {

    private Long userId;
    private String newPassword;
    private String confirmPassword;

}
