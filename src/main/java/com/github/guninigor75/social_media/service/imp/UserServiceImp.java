package com.github.guninigor75.social_media.service.imp;

import com.github.guninigor75.social_media.entity.activity.Message;
import com.github.guninigor75.social_media.entity.user.Friend;
import com.github.guninigor75.social_media.exception_handler.ResourceNotFoundException;
import com.github.guninigor75.social_media.entity.user.Role;
import com.github.guninigor75.social_media.entity.user.User;
import com.github.guninigor75.social_media.repository.UserRepository;
import com.github.guninigor75.social_media.security.SecurityUser;
import com.github.guninigor75.social_media.service.FriendService;
import com.github.guninigor75.social_media.service.MessageService;
import com.github.guninigor75.social_media.service.RoleService;
import com.github.guninigor75.social_media.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;

    private final FriendService friendService;

    private final MessageService messageService;

    private final RoleService roleService;

    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() ->{
            String message = String.format("User with id %d not found ", id);
            log.debug(message);
            return new ResourceNotFoundException(message);
        });
    }

    @Override
    @Transactional(readOnly = true)
    public User getByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> {
            String message = String.format("User with username %s not found ", username);
            log.debug(message);
            return new ResourceNotFoundException(message);
        });
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

    @Override
    @Transactional
    public void createFriendship(SecurityUser securityUser, Long friendId) {
        checkingCandidate(securityUser.getId(), friendId);
        User ownerRequest = getProxyUser(securityUser.getId());
        User target = getUserById(friendId);
        Friend  friend = new Friend();
        friend.setUser(ownerRequest);
        friend.setFriend(target);
        friendService.createFriend(friend);
    }

    @Override
    @Transactional
    public void deleteFriend(SecurityUser securityUser, Long friendId) {
        checkingCandidate(securityUser.getId(), friendId);
        friendService.deleteFriend(securityUser.getId(), friendId);
    }

    @Override
    @Transactional
    public Message createMessage(SecurityUser securityUser, Long friendId, Message message) {
        Long userId = securityUser.getId();
        checkingIsFriend(userId, friendId);
        User sender = getProxyUser(userId);
        User recipient = getUserById(friendId);
        message.setSender(sender);
        message.setRecipient(recipient);
        return messageService.createMessage(message);
    }

    @Override
    public List<Message> getMessages(SecurityUser securityUser, Pageable pageable) {
        Long senderId = securityUser.getId();
        return messageService.getMessages(senderId, pageable);
    }

    @Override
    public List<Message> getMessagesUser(SecurityUser securityUser, Long recipientId, Pageable pageable) {
        Long senderId = securityUser.getId();
        return messageService.getMessagesUser(senderId, recipientId, pageable);
    }

    private void checkingCandidate(Long userId, Long friendId) {
        if (Objects.equals(userId, friendId)) {
            String message = String.format("UserId %d == FriendId %d  unacceptable",userId, friendId);
            log.debug(message);
            throw new IllegalStateException(message);
        }
    }

    private void checkingIsFriend(Long userId, Long friendId) {
        boolean check = friendService.isFriend(userId, friendId);
        if (!check) {
            String message = String.format("Пользователь с id %d не является другом", friendId);
            log.debug(message);
            throw new IllegalStateException(message);
        }
    }
}
