package vn.vanquang239dn.service;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

import vn.vanquang239dn.model.enums.TokenType;

public interface JwtService {

    String generateAccessToken(long userId, String username, Collection<? extends GrantedAuthority> authorities);

    String generateRefreshToken(long userId, String username, Collection<? extends GrantedAuthority> authorities);

    String extractUsername(String token, TokenType tokenType);
}
