package com.github.guninigor75.social_media.service;

import com.github.guninigor75.social_media.entity.user.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    User getById(Long id);

    User getByUsername(String username);

    User createUser(User user);

    User getProxyUser(Long id);
}
