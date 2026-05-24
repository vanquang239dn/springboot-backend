package vn.vanquang239dn.service.impl;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jwts.SIG;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.vanquang239dn.config.properties.JwtProperties;
import vn.vanquang239dn.model.claims.AccessTokenClaims;
import vn.vanquang239dn.model.claims.RefreshTokenClaims;
import vn.vanquang239dn.model.entity.RefreshTokenEntity;
import vn.vanquang239dn.model.enums.TokenType;
import vn.vanquang239dn.model.principal.CustomUserPrincipal;
import vn.vanquang239dn.repository.RefreshTokenRepository;
import vn.vanquang239dn.service.JwtService;

@Service
@RequiredArgsConstructor
@ConfigurationPropertiesScan
@Slf4j(topic = "JWT-SERVICE")
public class JwtServiceImpl implements JwtService {

    private final JwtProperties jwtProperties;
    private final ObjectMapper objectMapper;
    private final RefreshTokenRepository refreshTokenRepository;
    private SecretKey accessKey;
    private SecretKey refreshKey;

    @PostConstruct
    void init() {
        accessKey = Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(jwtProperties.accessSecretKey()));

        refreshKey = Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(jwtProperties.refreshSecretKey()));
    }

    @Override
    public String generateAccessToken(CustomUserPrincipal userPrincipal) {

        log.info("Generate access token for user {} ", userPrincipal.getUserId());

        // Access token expiration time
        Instant now = Instant.now();
        Instant accessTokenExpiredAt = now.plusMillis(Duration.ofMinutes(jwtProperties.expireMinutes()).toMillis());

        return Jwts.builder()
                .subject(userPrincipal.getUserId().toString())
                .claims(toClaimsMap(buildAccessTokenClaims(userPrincipal)))
                .issuedAt(Date.from(now))
                .expiration(Date.from(accessTokenExpiredAt))
                .signWith(getSecretKey(TokenType.ACCESS_TOKEN), SIG.HS256)
                .compact();
    }

    @Override
    public String generateRefreshTokenForLogin(CustomUserPrincipal userPrincipal) {

        log.info("Generate refresh token for user {} for login ", userPrincipal.getUserId());

        // Refresh token expiration time
        Instant now = Instant.now();
        Instant refreshTokenExpiredAt = now.plusMillis(Duration.ofDays(jwtProperties.expireDays()).toMillis());

        // Create new jwtID
        String jwtId = UUID.randomUUID().toString();

        // Create new sessionId
        String sessionId = UUID.randomUUID().toString();

        RefreshTokenEntity refreshTokenEntity = RefreshTokenEntity.builder()
                .userId(userPrincipal.getUserId())
                .jwtId(jwtId)
                .sessionId(sessionId)
                .expiredAt(refreshTokenExpiredAt)
                .build();

        // Save refresh token to DB
        refreshTokenRepository.save(refreshTokenEntity);

        return Jwts.builder()
                .subject(userPrincipal.getUserId().toString())
                .claims(toClaimsMap(buildRefreshTokenClaims(jwtId, sessionId)))
                .issuedAt(Date.from(now))
                .expiration(Date.from(refreshTokenExpiredAt))
                .signWith(getSecretKey(TokenType.REFRESH_TOKEN), SIG.HS256)
                .compact();
    }

    @Override
    public String generateRefreshTokenForRefresh(CustomUserPrincipal userPrincipal, String oldSessionId) {

        log.info("Generate refresh token for user {} for refresh", userPrincipal.getUserId());

        // Refresh token expiration time
        Instant now = Instant.now();
        Instant refreshTokenExpiredAt = now.plusMillis(Duration.ofDays(jwtProperties.expireDays()).toMillis());

        // Create new jwtID
        String jwtId = UUID.randomUUID().toString();

        RefreshTokenEntity refreshTokenEntity = RefreshTokenEntity.builder()
                .userId(userPrincipal.getUserId())
                .jwtId(jwtId)
                .sessionId(oldSessionId)
                .expiredAt(refreshTokenExpiredAt)
                .build();

        // Save refresh token to DB
        refreshTokenRepository.save(refreshTokenEntity);

        return Jwts.builder()
                .subject(userPrincipal.getUserId().toString())
                .claims(toClaimsMap(buildRefreshTokenClaims(jwtId, oldSessionId)))
                .issuedAt(Date.from(now))
                .expiration(Date.from(refreshTokenExpiredAt))
                .signWith(getSecretKey(TokenType.REFRESH_TOKEN), SIG.HS256)
                .compact();
    }

    @Override
    public String extractSubject(String token, TokenType type) {
        return extractClaim(token, type, Claims::getSubject);
    }

    @Override
    public Claims extractAllClaims(String token, TokenType type) {
        return Jwts.parser()
                .verifyWith(getSecretKey(type))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private <T> T extractClaim(String token, TokenType type, Function<Claims, T> claimsExtractor) {
        final Claims claims = extractAllClaims(token, type);
        return claimsExtractor.apply(claims);
    }

    private AccessTokenClaims buildAccessTokenClaims(CustomUserPrincipal userPrincipal) {
        return AccessTokenClaims.builder()
                .username(userPrincipal.getUsername())
                .authorities(userPrincipal.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .build();
    }

    private RefreshTokenClaims buildRefreshTokenClaims(String jwtId, String sessionId) {
        return RefreshTokenClaims.builder()
                .jwtId(jwtId)
                .sessionId(sessionId)
                .build();
    }

    private Map<String, Object> toClaimsMap(Object claims) {
        return objectMapper.convertValue(claims, new TypeReference<Map<String, Object>>() {
        });
    }

    private SecretKey getSecretKey(TokenType tokenType) {
        return switch (tokenType) {
            case ACCESS_TOKEN -> accessKey;
            case REFRESH_TOKEN -> refreshKey;
        };
    }
}