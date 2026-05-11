package vn.vanquang239dn.dto.request;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.vanquang239dn.model.enums.Gender;
import vn.vanquang239dn.model.enums.UserType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserCreationRequest implements Serializable {

    @NotBlank(message = "First name must be not blank")
    private String firstName;

    @NotBlank(message = "Last name must be not blank")
    private String lastName;
    private Gender gender;
    private LocalDate birthday;

    @Email(message = "Email invalid")
    private String email;
    private String phone;
    private String username;
    private UserType type;
    private List<AddressRequest> addresses;

}
