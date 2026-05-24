package vn.vanquang239dn.dto.request;

import java.io.Serializable;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserPasswordUpdateRequest implements Serializable {

    @NotNull(message = "Id must be not null")
    @Min(value = 1, message = "User ID must be equals or greater than 1")
    private final Long userId;

    @NotBlank(message = "New password must be not blank")
    private final String newPassword;

    @NotBlank(message = "New password must be not blank")
    private final String confirmPassword;

}
