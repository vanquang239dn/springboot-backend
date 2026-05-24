package vn.vanquang239dn.dto.request;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignInRequest implements Serializable {

        @NotBlank(message = "Username must be not blank")
        private final String username;

        @NotBlank(message = "Password must be not blank")
        private final String password;

        @NotBlank(message = "Password must be not blank")
        private final String platform;

        @NotBlank(message = "Password must be not blank")
        private final String deviceToken;

        @NotBlank(message = "Password must be not blank")
        private final String versionApp;
}
