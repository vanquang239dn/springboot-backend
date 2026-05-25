package vn.vanquang239dn.controller;

import java.io.IOException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.vanquang239dn.dto.request.MailSendingRequest;
import vn.vanquang239dn.service.impl.EmailServiceImpl;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
@Slf4j(topic = "EMAIL-CONTROLLER")
@Tag(name = "Email Controller", description = "Controller for sending email")
public class EmailController {

    private final EmailServiceImpl emailService;

    @PostMapping("/send")
    @PreAuthorize("hasAuthority('MAIL_SEND')")
    public void send(@RequestBody MailSendingRequest emailRequest) throws IOException {

        log.info("Sending email to {}", emailRequest.getReceiverEmail());

        emailService.sendEmail(emailRequest.getReceiverEmail(), emailRequest.getSubject(), emailRequest.getContent());

        log.info("Email sent");
    }
}
