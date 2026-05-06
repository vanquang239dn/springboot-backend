package vn.vanquang239dn.controller.response;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import vn.vanquang239dn.model.enums.Gender;

@Getter
@Setter
@AllArgsConstructor
public class UserResponse implements Serializable {
    private long id;
    private String username;
    private String firstName;
    private String lastName;
    private Gender gender;
    private Date birthday;
    private String email;
    private String phone;
}
