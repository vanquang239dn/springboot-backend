package vn.vanquang239dn.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.sendgrid")
public record MailProperties(
                String apiKey,
                String senderEmail) {
}