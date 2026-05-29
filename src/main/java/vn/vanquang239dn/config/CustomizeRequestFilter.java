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
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
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
    private static final String TRACE_ID_HEADER = "X-Trace-Id";
    private final Tracer tracer;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Get current span
        Span currentSpan = tracer.currentSpan();

        // When span is not null
        if (currentSpan != null) {
            response.setHeader(TRACE_ID_HEADER, currentSpan.context().traceId());
        }

        try {

            log.info("{} {} ", request.getMethod(), request.getRequestURI());

            // Get access token from request
            String accessToken = extractBearerToken(request);

            if (accessToken == null) {

                // If access token is null do filter chain
                filterChain.doFilter(request, response);
                return;
            } else {
                // If access token is not null, authenticate user
                // If authenticate failed, return
                if (!authenticateRequest(accessToken, request, response)) {
                    return;
                }
            }

            // If authenticate successfully, do filter chain
            filterChain.doFilter(request, response);

        } catch (ExpiredJwtException e) {
            writeErrorResponse(request, response, HttpStatus.UNAUTHORIZED, "Token expired", null);

        } catch (SignatureException e) {
            writeErrorResponse(request, response, HttpStatus.UNAUTHORIZED, "Invalid token signature", null);

        } catch (JwtException e) {
            writeErrorResponse(request, response, HttpStatus.UNAUTHORIZED, "Invalid token", null);

        } catch (UsernameNotFoundException e) {
            writeErrorResponse(request, response, HttpStatus.UNAUTHORIZED, "User not found", null);

        }

    }

    private String extractBearerToken(HttpServletRequest request) {

        // Get authorization header from request
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {

            // Return access token
            return authHeader.substring(7);

        } else {

            // Return null if cant get access token
            return null;
        }
    }

    private boolean authenticateRequest(String accessToken, HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        // avoid re-authentication
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            return true;
        }

        // Parse jwt claim
        Claims claims = jwtService.extractAllClaims(accessToken, TokenType.ACCESS_TOKEN);

        // Get user name from claims
        String username = claims.get("username", String.class);

        // Check username
        if (username == null || username.isBlank()) {
            writeErrorResponse(request, response, HttpStatus.UNAUTHORIZED, "Invalid token", null);
            return false;
        }

        // Verify user by username
        CustomUserPrincipal userPrincipal = customUserDetailsService.loadUserByUsername(username);

        // Check user is enabled
        if (!userPrincipal.isEnabled()) {
            writeErrorResponse(request, response, HttpStatus.UNAUTHORIZED, "User inactive", null);
            return false;
        }

        setAuthentication(request, userPrincipal);

        return true;
    }

    private void setAuthentication(HttpServletRequest request, CustomUserPrincipal userPrincipal) {

        // Create an empty context
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

        // Authenticate user
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userPrincipal, null, userPrincipal.getAuthorities());

        // Set authentication token into security context holder
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        securityContext.setAuthentication(authenticationToken);
        SecurityContextHolder.setContext(securityContext);
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
