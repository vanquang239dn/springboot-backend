package vn.vanquang239dn.controller.request;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.hibernate.usertype.UserType;

import lombok.Getter;
import vn.vanquang239dn.model.enums.Gender;

@Getter
public class UserCreationRequest implements Serializable {

    private String firstName;
    private String lastName;
    private Gender gender;
    private Date birthday;
    private String email;
    private String phone;
    private String username;
    private UserType type;
    private List<AddressRequest> addresses;

}
