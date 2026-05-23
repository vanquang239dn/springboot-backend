package vn.vanquang239dn.config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

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
import vn.vanquang239dn.model.entity.CustomUserPrincipal;
import vn.vanquang239dn.model.enums.TokenType;
import vn.vanquang239dn.service.impl.CustomUserDetailsService;
import vn.vanquang239dn.service.impl.JwtServiceImpl;

@Component
@Slf4j(topic = "CUSTOMIZE-REQUEST-FILTER")
@RequiredArgsConstructor
public class CustomizeRequestFilter extends OncePerRequestFilter {

    private final JwtServiceImpl jwtService;
    private final CustomUserDetailsService customerUserDetailsService;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        log.info("{} {} ", request.getMethod(), request.getRequestURI());

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String accessToken = authHeader.substring(7);

            try {
                // Get username by token
                String username = jwtService.extractUsername(accessToken, TokenType.ACCESS_TOKEN);

                // avoid re-authentication
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                    // Verify user by username
                    CustomUserPrincipal userPrincipal = customerUserDetailsService.loadUserByUsername(username);

                    // Create an empty context
                    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

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

        response.setStatus(status.value());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));

        response.flushBuffer();
    }
}
