package vn.vanquang239dn.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.jwt")
public record JwtProperties(
        long expireMinutes,
        long expireDays,
        String accessSecretKey,
        String refreshSecretKey) {
}