package com.github.guninigor75.social_media.service;

import com.github.guninigor75.social_media.entity.user.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public interface UserService {

    User getUserById(Long id);

    User getByUsername(String username);

    User createUser(User user);

    User getLinkUser(Long id);


    void createRequestFriendship(Long userId, Long friendId);

    void acceptedFriendShip(Long userId, Long candidate);


    void deleteFriend(Long userId, Long friendId);

    void rejectedFriendShip(Long userId, Long candidate);
}
