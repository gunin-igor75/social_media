package com.github.guninigor75.social_media.service.imp;

import com.github.guninigor75.social_media.exception.ResourceNotFoundException;
import com.github.guninigor75.social_media.entity.user.Role;
import com.github.guninigor75.social_media.entity.user.User;
import com.github.guninigor75.social_media.repository.UserRepository;
import com.github.guninigor75.social_media.service.RoleService;
import com.github.guninigor75.social_media.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;

    private final RoleService roleService;

    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public User getById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(
                        String.format("User with id %d not found ", id)
                )
        );
    }

    @Override
    @Transactional(readOnly = true)
    public User getByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new ResourceNotFoundException(
                        String.format("User with username %s not found ", username)
                )
        );
    }

    @Override
    @Transactional
    public User createUser(User user) {
        Optional<User> userOrEmpty = userRepository.findByUsername(user.getUsername());
        if (userOrEmpty.isPresent()) {
            String message = String.format("User with this email %s  already exists", user.getUsername());
            log.debug(message);
            throw new IllegalStateException(message);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role role = roleService.getRoleByName("ROLE_USER");
        user.addRole(role);
        return userRepository.save(user);
    }

    @Override
    public User getProxyUser(Long id) {
        return userRepository.getReferenceById(id);
    }
}
