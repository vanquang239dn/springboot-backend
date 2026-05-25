package vn.vanquang239dn.service.impl;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.vanquang239dn.dto.request.SignInRequest;
import vn.vanquang239dn.dto.response.TokenResponse;
import vn.vanquang239dn.model.entity.RefreshTokenEntity;
import vn.vanquang239dn.model.entity.UserEntity;
import vn.vanquang239dn.model.enums.TokenType;
import vn.vanquang239dn.model.enums.UserStatus;
import vn.vanquang239dn.model.principal.CustomUserPrincipal;
import vn.vanquang239dn.repository.RefreshTokenRepository;
import vn.vanquang239dn.repository.UserRepository;
import vn.vanquang239dn.service.AuthenticationService;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "AUTHENTICATION-SERVICE")
public class AuthenticationServiceImpl implements AuthenticationService {

        private final UserRepository userRepository;
        private final RefreshTokenRepository refreshTokenRepository;
        private final CustomUserDetailsService customUserDetailsService;
        private final AuthenticationManager authenticationManager;
        private final JwtServiceImpl jwtService;
        private final SecurityEventServiceImpl securityEventService;

        @Override
        public TokenResponse authenticate(SignInRequest signInRequest) {

                log.info("Authenticate user");

                try {

                        log.info("Login username={}", signInRequest.getUsername());

                        // Authenticate username and password
                        Authentication authentication = authenticationManager.authenticate(
                                        new UsernamePasswordAuthenticationToken(signInRequest.getUsername(),
                                                        signInRequest.getPassword()));

                        // Set to security context holder
                        SecurityContextHolder.getContext().setAuthentication(authentication);

                        // After authenticated successfully get customUserPrincipal
                        CustomUserPrincipal userPrincipal = (CustomUserPrincipal) authentication.getPrincipal();

                        // Generate access token
                        String accessToken = jwtService.generateAccessToken(userPrincipal);

                        // Generate refresh token
                        String refreshToken = jwtService.generateRefreshTokenForLogin(userPrincipal);

                        return TokenResponse.builder()
                                        .accessToken(accessToken)
                                        .refreshToken(refreshToken)
                                        .build();

                } catch (AuthenticationException e) {
                        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
                }
        }

        @Override
        @Transactional(rollbackFor = Exception.class)
        public TokenResponse refreshToken(String refreshToken) {

                log.info("Refresh access token and refresh token");

                // Get now
                Instant now = Instant.now();

                try {

                        // Parse jwt claim
                        Claims claims = jwtService.extractAllClaims(refreshToken, TokenType.REFRESH_TOKEN);

                        // Get user Id from claims
                        Long userId = Long.valueOf(claims.getSubject());

                        // Get jwt Id from claims
                        String jwtId = claims.get("jwtId", String.class);

                        // Get session Id from claims
                        String sessionId = claims.get("sessionId", String.class);

                        // Find old refresh token by jwt Id
                        RefreshTokenEntity oldRefreshToken = refreshTokenRepository.findByJwtId(jwtId)
                                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                                                        "Invalid refresh token"));

                        // Check set of user id and session id
                        if (!oldRefreshToken.getUserId().equals(userId)
                                        || !oldRefreshToken.getSessionId().equals(sessionId)) {
                                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token");
                        }

                        // Check old refresh token isRevoke
                        if (oldRefreshToken.isRevoked()) {
                                securityEventService.revokeBySessionId(sessionId, now, "REUSE DETECTED");
                                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token reused");
                        }

                        // Check old refresh token is expired
                        if (oldRefreshToken.getExpiredAt().isBefore(now)) {
                                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token expired");
                        }

                        // Verify user present
                        UserEntity user = userRepository.findById(userId)
                                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                                                        "User not found"));

                        // Verify user status
                        if (user.getStatus() != UserStatus.ACTIVE) {
                                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User inactive");
                        }

                        // Get customUserPrincipal
                        CustomUserPrincipal userPrincipal = customUserDetailsService
                                        .loadUserByUsername(user.getUsername());

                        // Rotate old refresh token
                        oldRefreshToken.setRevoked(true);
                        oldRefreshToken.setRevokedAt(now);
                        oldRefreshToken.setRevokeReason("ROTATED");
                        refreshTokenRepository.save(oldRefreshToken);

                        log.info("Refresh token rotated successful");

                        // Generate new access token
                        String newAccessToken = jwtService.generateAccessToken(userPrincipal);

                        // Generate new refresh token
                        String newRefreshToken = jwtService.generateRefreshTokenForRefresh(userPrincipal, sessionId);

                        return TokenResponse.builder()
                                        .accessToken(newAccessToken)
                                        .refreshToken(newRefreshToken)
                                        .build();

                } catch (ExpiredJwtException e) {
                        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token expired");
                } catch (JwtException e) {
                        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token");
                }
        }

}
