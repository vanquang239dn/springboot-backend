package vn.vanquang239dn.model.principal;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import vn.vanquang239dn.model.enums.UserStatus;

@Getter
@Builder
@RequiredArgsConstructor
public class CustomUserPrincipal implements UserDetails {

    private final Long userId;

    private final String username;

    private final String password;

    private final UserStatus userStatus;

    private final Collection<? extends GrantedAuthority> authorities;

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return UserStatus.ACTIVE.equals(userStatus);
    }
}