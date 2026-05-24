package vn.vanquang239dn.dto.request;

import java.io.Serializable;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MailSendingRequest implements Serializable {

        @Email(message = "Email is invalid")
        private final String receiverEmail;

        @NotBlank(message = "Subject must be not blank")
        private final String subject;

        @NotBlank(message = "Content must be not blank")
        private final String content;
}
