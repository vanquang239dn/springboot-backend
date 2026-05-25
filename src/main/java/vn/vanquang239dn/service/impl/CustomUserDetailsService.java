package vn.vanquang239dn.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.vanquang239dn.model.entity.UserEntity;
import vn.vanquang239dn.model.principal.CustomUserPrincipal;
import vn.vanquang239dn.repository.PermissionRepository;
import vn.vanquang239dn.repository.RoleRepository;
import vn.vanquang239dn.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

        private final UserRepository userRepository;
        private final RoleRepository roleRepository;
        private final PermissionRepository permissionRepository;

        @Override
        public CustomUserPrincipal loadUserByUsername(String username) throws UsernameNotFoundException {

                // Get user by username
                UserEntity user = userRepository.findByUsername(username)
                                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

                // Create an empty authorities instance
                List<GrantedAuthority> authorities = new ArrayList<>();

                // Get all roles then Add to authorities with format "ROLE_ + role"
                roleRepository.findAllRolesByUserId(user.getUserId())
                                .stream()
                                .map(role -> "ROLE_" + role)
                                .map(SimpleGrantedAuthority::new)
                                .forEach(authorities::add);

                // Get permission from all roles then add to authorities
                permissionRepository.findAllPermissionsByUserId(user.getUserId())
                                .stream()
                                .map(SimpleGrantedAuthority::new)
                                .forEach(authorities::add);

                return CustomUserPrincipal.builder()
                                .userId(user.getUserId())
                                .username(user.getUsername())
                                .password(user.getPassword())
                                .userStatus(user.getStatus())
                                .authorities(authorities)
                                .build();
        }
}