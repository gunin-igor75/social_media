package com.github.guninigor75.social_media.service;

import com.github.guninigor75.social_media.entity.activity.Message;
import com.github.guninigor75.social_media.entity.user.User;
import com.github.guninigor75.social_media.security.SecurityUser;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public interface UserService {

    User getUserById(Long id);

    User getByUsername(String username);

    User createUser(User user);

    User getProxyUser(Long id);

    @Transactional
    void createFriendship(SecurityUser securityUser, Long friendId);

    void deleteFriend(SecurityUser securityUser, Long friendId);

    @Transactional
    Message createMessage(SecurityUser securityUser, Long friendId, Message message);

    List<Message> getMessages(SecurityUser securityUser, Pageable pageable);

    List<Message> getMessagesUser(SecurityUser securityUser, Long friendId, Pageable pageable);

}
