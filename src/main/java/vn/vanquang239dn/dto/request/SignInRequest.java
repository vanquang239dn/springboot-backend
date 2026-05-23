package vn.vanquang239dn.dto.request;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignInRequest implements Serializable {

    @NotBlank(message = "Username must be not blank")
    private String username;

    @NotBlank(message = "Password must be not blank")
    private String password;

    @NotBlank(message = "Password must be not blank")
    private String platform;

    @NotBlank(message = "Password must be not blank")
    private String deviceToken;

    @NotBlank(message = "Password must be not blank")
    private String versionApp;
}
