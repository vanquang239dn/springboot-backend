package vn.vanquang239dn.service;

import io.jsonwebtoken.Claims;
import vn.vanquang239dn.model.enums.TokenType;
import vn.vanquang239dn.model.principal.CustomUserPrincipal;

public interface JwtService {

    String generateRefreshTokenForLogin(CustomUserPrincipal userPrincipal);

    String generateRefreshTokenForRefresh(CustomUserPrincipal userPrincipal, String oldSessionId);

    String generateAccessToken(CustomUserPrincipal userPrincipal);

    String extractSubject(String token, TokenType tokenType);

    Claims extractAllClaims(String token, TokenType type);
}
