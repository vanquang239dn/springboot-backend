package vn.vanquang239dn.dto.request;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import lombok.Getter;
import vn.vanquang239dn.model.enums.Gender;

@Getter
public class UserUpdateRequest implements Serializable {

    private long userId;
    private String username;
    private String firstName;
    private String lastName;
    private Gender gender;
    private LocalDate birthday;
    private String email;
    private String phone;
    private List<AddressRequest> addresses;

}
