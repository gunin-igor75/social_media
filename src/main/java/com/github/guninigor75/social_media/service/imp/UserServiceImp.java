package com.github.guninigor75.social_media.service.imp;

import com.github.guninigor75.social_media.domain.user.User;
import com.github.guninigor75.social_media.repository.UserRepository;
import com.github.guninigor75.social_media.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;

    @Override
    public User getById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new UsernameNotFoundException(
                        String.format("User with id %d not found ", id)
                )
        );
    }
}
