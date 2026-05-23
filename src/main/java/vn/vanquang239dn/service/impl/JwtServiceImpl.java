package vn.vanquang239dn.service.impl;

import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jwts.SIG;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.vanquang239dn.config.properties.JwtProperties;
import vn.vanquang239dn.model.enums.TokenType;
import vn.vanquang239dn.service.JwtService;

@Service
@RequiredArgsConstructor
@ConfigurationPropertiesScan
@Slf4j(topic = "JWT-SERVICE")
public class JwtServiceImpl implements JwtService {

    private final JwtProperties jwtProperties;
    private SecretKey accessKey;
    private SecretKey refreshKey;

    @PostConstruct
    void init() {
        accessKey = Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(jwtProperties.accessSecretKey()));

        refreshKey = Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(jwtProperties.refreshSecretKey()));
    }

    // Generate Access Token
    @Override
    public String generateAccessToken(long userId, String username,
            Collection<? extends GrantedAuthority> authorities) {

        log.info("Generate access token for user {} with authorities {}", userId, authorities);

        Map<String, Object> claims = buildClaims(userId, authorities);

        return generateToken(claims, username, TokenType.ACCESS_TOKEN,
                Duration.ofMinutes(jwtProperties.expireMinutes()).toMillis());
    }

    // Generate Refresh Token
    @Override
    public String generateRefreshToken(long userId, String username,
            Collection<? extends GrantedAuthority> authorities) {

        log.info("Generate refresh token for user {} with authorities {}", userId, authorities);

        Map<String, Object> claims = buildClaims(userId, authorities);

        return generateToken(claims, username, TokenType.REFRESH_TOKEN,
                Duration.ofDays(jwtProperties.expireDays()).toMillis());
    }

    @Override
    public String extractUsername(String token, TokenType type) {
        return extractClaim(token, type, Claims::getSubject);
    }

    public <T> T extractClaim(String token, TokenType type, Function<Claims, T> claimsExtractor) {
        final Claims claims = extractAllClaims(token, type);
        return claimsExtractor.apply(claims);
    }

    public Claims extractAllClaims(String token, TokenType type) {
        return Jwts.parser()
                .verifyWith(getSecretKey(type))
                .build()
                .parseSignedClaims(token)
                .getPayload();

    }

    private String generateToken(Map<String, Object> claims, String username, TokenType tokenType,
            long expirationMillis) {

        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusMillis(expirationMillis)))
                .signWith(getSecretKey(tokenType), SIG.HS256)
                .compact();
    }

    private SecretKey getSecretKey(TokenType tokenType) {
        return switch (tokenType) {
            case ACCESS_TOKEN -> accessKey;
            case REFRESH_TOKEN -> refreshKey;
        };
    }

    private Map<String, Object> buildClaims(long userId, Collection<? extends GrantedAuthority> authorities) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("roles", authorities.stream().map(GrantedAuthority::getAuthority).toList());

        return claims;
    }
}