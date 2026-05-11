package vn.vanquang239dn.dto.request;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import vn.vanquang239dn.model.enums.Gender;

@Getter
public class UserUpdateRequest implements Serializable {

    @NotNull(message = "Id must be not null")
    @Min(value = 1, message = "User ID must be equals or greater than 1")
    private long userId;

    @NotBlank(message = "User name must be not blank")
    private String username;

    @NotBlank(message = "First name must be not blank")
    private String firstName;

    @NotBlank(message = "Last name must be not blank")
    private String lastName;
    private Gender gender;
    private LocalDate birthday;
    private String email;
    private String phone;
    private List<AddressRequest> addresses;

}
