package vn.vanquang239dn.dto.response;

import java.io.Serializable;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import vn.vanquang239dn.model.enums.Gender;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserResponse implements Serializable {
    private long id;
    private String firstName;
    private String lastName;
    private Gender gender;
    private LocalDate birthday;
    private String email;
    private String phone;
    private String username;
}
