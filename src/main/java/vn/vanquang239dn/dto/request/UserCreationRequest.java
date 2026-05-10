package vn.vanquang239dn.dto.request;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import lombok.Getter;
import vn.vanquang239dn.model.enums.Gender;
import vn.vanquang239dn.model.enums.UserType;

@Getter
public class UserCreationRequest implements Serializable {

    private String firstName;
    private String lastName;
    private Gender gender;
    private LocalDate birthday;
    private String email;
    private String phone;
    private String username;
    private UserType type;
    private List<AddressRequest> addresses;

}
