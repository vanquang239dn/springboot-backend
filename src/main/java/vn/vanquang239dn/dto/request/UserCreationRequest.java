package vn.vanquang239dn.dto.request;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import vn.vanquang239dn.model.enums.Gender;

@Getter
@AllArgsConstructor
public class UserCreationRequest implements Serializable {

        @NotBlank(message = "First name must be not blank")
        private final String firstName;

        @NotBlank(message = "Last name must be not blank")
        private final String lastName;

        private final Gender gender;

        private final LocalDate birthday;

        @Email(message = "Email invalid")
        private final String email;

        private final String phone;

        @NotBlank(message = "User name must be not blank")
        private final String username;

        @NotBlank(message = "Password must be not blank")
        private final String password;

        private final List<AddressRequest> addresses;

}
