package vn.vanquang239dn.dto.request;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import vn.vanquang239dn.model.enums.Gender;

@Getter
@AllArgsConstructor
public class UserUpdateRequest implements Serializable {

    @NotNull(message = "Id must be not null")
    @Min(value = 1, message = "User ID must be equals or greater than 1")
    private final long userId;

    @NotBlank(message = "User name must be not blank")
    private final String username;

    @NotBlank(message = "First name must be not blank")
    private final String firstName;

    @NotBlank(message = "Last name must be not blank")
    private final String lastName;
    private final Gender gender;
    private final LocalDate birthday;
    private final String email;
    private final String phone;
    private final List<AddressRequest> addresses;

}
