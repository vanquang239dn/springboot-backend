package vn.vanquang239dn.config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.vanquang239dn.dto.response.ExceptionResponse;
import vn.vanquang239dn.model.enums.TokenType;
import vn.vanquang239dn.model.principal.CustomUserPrincipal;
import vn.vanquang239dn.service.impl.CustomUserDetailsService;
import vn.vanquang239dn.service.impl.JwtServiceImpl;

@Component
@Slf4j(topic = "CUSTOMIZE-FILTER")
@RequiredArgsConstructor
public class CustomizeRequestFilter extends OncePerRequestFilter {

    private final JwtServiceImpl jwtService;
    private final CustomUserDetailsService customUserDetailsService;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        log.info("{} {} ", request.getMethod(), request.getRequestURI());

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {

            try {

                // Get access token from header
                String accessToken = authHeader.substring(7);

                // Parse jwt claim
                Claims claims = jwtService.extractAllClaims(accessToken, TokenType.ACCESS_TOKEN);

                // Get user name from claims
                String username = claims.get("username", String.class);

                // Check username
                if (username == null || username.isBlank()) {
                    writeErrorResponse(request, response, HttpStatus.UNAUTHORIZED, "Invalid token", null);
                    return;
                }

                // avoid re-authentication
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                    // Verify user by username
                    CustomUserPrincipal userPrincipal = customUserDetailsService.loadUserByUsername(username);

                    // Check user is enabled
                    if (!userPrincipal.isEnabled()) {
                        writeErrorResponse(request, response, HttpStatus.UNAUTHORIZED, "User inactive", null);
                        return;
                    }

                    // Create an empty context
                    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

                    // Authenticate user
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            userPrincipal, null, userPrincipal.getAuthorities());

                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    securityContext.setAuthentication(authenticationToken);
                    SecurityContextHolder.setContext(securityContext);
                }

            } catch (ExpiredJwtException e) {
                writeErrorResponse(request, response, HttpStatus.UNAUTHORIZED, "Token expired", null);
                return;

            } catch (SignatureException e) {
                writeErrorResponse(request, response, HttpStatus.UNAUTHORIZED, "Invalid token signature", null);
                return;

            } catch (JwtException e) {
                writeErrorResponse(request, response, HttpStatus.UNAUTHORIZED, "Invalid token", null);
                return;

            } catch (UsernameNotFoundException e) {
                writeErrorResponse(request, response, HttpStatus.UNAUTHORIZED, "User not found", null);
                return;
            }
        }

        filterChain.doFilter(request, response);

    }

    private void writeErrorResponse(HttpServletRequest request, HttpServletResponse response, HttpStatus status,
            String message, Object details) throws IOException {

        ExceptionResponse errorResponse = ExceptionResponse.builder()
                .timestamp(Instant.now())
                .status(status.value())
                .path(request.getRequestURI())
                .message(message)
                .details(details)
                .build();

        response.resetBuffer();

        response.setStatus(HttpStatus.OK.value());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));

        response.flushBuffer();
    }
}
