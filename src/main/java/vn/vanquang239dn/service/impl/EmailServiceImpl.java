package vn.vanquang239dn.service.impl;

import java.io.IOException;

import org.springframework.stereotype.Service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.vanquang239dn.config.properties.MailProperties;
import vn.vanquang239dn.service.EmailService;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "EMAIL-SERVICE")
public class EmailServiceImpl implements EmailService {

    private final MailProperties mailProperties;
    private final SendGrid sendGrid;

    /**
     * Send email by SendGrid
     * 
     * @param receiverEmail
     * @param subject
     * @param text
     * @throws IOException
     */
    public void sendEmail(String receiverEmail, String subject, String text) throws IOException {
        Email fromEmail = new Email(mailProperties.senderEmail());
        Email toEmail = new Email(receiverEmail);

        Content content = new Content("text/plain", text);
        Mail mail = new Mail(fromEmail, subject, toEmail, content);

        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sendGrid.api(request);

            if (response.getStatusCode() == 202) {
                log.info("Email sent successfully");
            } else {
                log.info("Email sent failed");
            }

            log.info("Status code: {}", response.getStatusCode());
            log.info("Response body: {}", response.getBody());
            log.info("Headers: {}", response.getHeaders());

        } catch (IOException e) {
            log.error("Error happened while sending email : {}", e.getMessage());
        }

    }
}