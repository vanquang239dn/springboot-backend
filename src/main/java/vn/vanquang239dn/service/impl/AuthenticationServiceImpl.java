package vn.vanquang239dn.service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.vanquang239dn.dto.request.SignInRequest;
import vn.vanquang239dn.dto.response.TokenResponse;
import vn.vanquang239dn.model.entity.CustomUserPrincipal;
import vn.vanquang239dn.model.entity.UserEntity;
import vn.vanquang239dn.model.enums.TokenType;
import vn.vanquang239dn.model.enums.UserStatus;
import vn.vanquang239dn.repository.UserRepository;
import vn.vanquang239dn.service.AuthenticationService;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "AUTHENTICATION-SERVICE")
public class AuthenticationServiceImpl implements AuthenticationService {

        private final UserRepository userRepository;
        private final AuthenticationManager authenticationManager;
        private final JwtServiceImpl jwtService;

        @Override
        public TokenResponse authenticate(SignInRequest signInRequest) {

                log.info("Get access token");

                try {
                        // Authenticate username and password
                        Authentication authentication = authenticationManager.authenticate(
                                        new UsernamePasswordAuthenticationToken(signInRequest.getUsername(),
                                                        signInRequest.getPassword()));

                        // Set to security context holder
                        SecurityContextHolder.getContext().setAuthentication(authentication);

                        // After authenticated successfully get customUserPrincipal
                        CustomUserPrincipal userPrincipal = (CustomUserPrincipal) authentication.getPrincipal();

                        // Generate access token
                        String accessToken = jwtService.generateAccessToken(userPrincipal.getUserId(),
                                        userPrincipal.getUsername(),
                                        userPrincipal.getAuthorities());

                        // Generate refresh token
                        String refreshToken = jwtService.generateRefreshToken(userPrincipal.getUserId(),
                                        userPrincipal.getUsername(),
                                        userPrincipal.getAuthorities());

                        return TokenResponse.builder()
                                        .accessToken(accessToken)
                                        .refreshToken(refreshToken)
                                        .build();

                } catch (AuthenticationException e) {
                        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
                }
        }

        @Override
        public TokenResponse refreshToken(String refreshToken) {

                log.info("Refresh access token and refresh token");

                try {
                        // Parse jwt claim
                        Claims claims = jwtService.extractAllClaims(refreshToken, TokenType.REFRESH_TOKEN);

                        Long userId = claims.get("userId", Long.class);

                        // Verify user present
                        UserEntity user = userRepository.findById(userId)
                                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                                                        "User not found"));

                        // Verify user status
                        if (user.getStatus() != UserStatus.ACTIVE) {
                                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User inactive");
                        }

                        // Authenticate username and password
                        Authentication authentication = authenticationManager.authenticate(
                                        new UsernamePasswordAuthenticationToken(user.getUsername(),
                                                        user.getPassword()));

                        // Set to security context holder
                        SecurityContextHolder.getContext().setAuthentication(authentication);

                        // After authenticated successfully get customUserPrincipal
                        CustomUserPrincipal userPrincipal = (CustomUserPrincipal) authentication.getPrincipal();

                        // Generate new access token
                        String newAccessToken = jwtService.generateAccessToken(userPrincipal.getUserId(),
                                        userPrincipal.getUsername(),
                                        userPrincipal.getAuthorities());

                        // Generate new refresh token
                        String newRefreshToken = jwtService.generateRefreshToken(userPrincipal.getUserId(),
                                        userPrincipal.getUsername(),
                                        userPrincipal.getAuthorities());

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
