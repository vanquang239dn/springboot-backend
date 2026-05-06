package vn.vanquang239dn.controller.request;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import vn.vanquang239dn.model.enums.Gender;

@Getter
public class UserCreationRequest implements Serializable {

    private String username;
    private String firstName;
    private String lastName;
    private Gender gender;
    private Date birthday;
    private String email;
    private String phone;

}
