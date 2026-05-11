package vn.vanquang239dn.dto.request;

import java.io.Serializable;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MailSendingRequest implements Serializable {

    @Email(message = "Email is invalid")
    private String receiverEmail;

    @NotBlank(message = "Subject must be not blank")
    private String subject;

    @NotBlank(message = "Content must be not blank")
    private String content;
}
