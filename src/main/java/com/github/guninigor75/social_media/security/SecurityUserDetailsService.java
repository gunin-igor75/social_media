package com.github.guninigor75.social_media.security;

import com.github.guninigor75.social_media.entity.user.Role;
import com.github.guninigor75.social_media.entity.user.User;
import com.github.guninigor75.social_media.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;


@Service
@RequiredArgsConstructor
public class SecurityUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException(
                        String.format("User named %s not found ", username)
                )
        );
        return new SecurityUser (
                user.getId(),
                user.getName(),
                user.getUsername(),
                user.getPassword(),
                getAuthority(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> getAuthority(Collection<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).toList();
    }
}
