package com.github.guninigor75.social_media.service.imp;

import com.github.guninigor75.social_media.entity.user.Invite;
import com.github.guninigor75.social_media.entity.user.Role;
import com.github.guninigor75.social_media.entity.user.User;
import com.github.guninigor75.social_media.exception_handler.ResourceNotFoundException;
import com.github.guninigor75.social_media.repository.UserRepository;
import com.github.guninigor75.social_media.service.InviteService;
import com.github.guninigor75.social_media.service.RoleService;
import com.github.guninigor75.social_media.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;

    private final RoleService roleService;

    private final InviteService inviteService;

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
    public User getLinkUser(Long id) {
        return userRepository.getReferenceById(id);
    }

    @Override
    @Transactional
    public void createRequestFriendship(Long userId, Long targetId) {
        checkingCandidateForFriendship(userId, targetId);
        Optional<Invite> inviteOrEmpty = inviteService.getInvite(userId, targetId);
        if (inviteOrEmpty.isEmpty()) {
            createSubscription(userId, targetId);
        } else {
            Invite invite = inviteOrEmpty.get();
            if (invite.getStatus() == Invite.Status.ACCEPTED) {
                String message = "You are already friends";
                log.debug(message);
                throw new IllegalStateException(message);
            } else {
                if (invite.getCandidate().equals(userId)) {
                    acceptedSubscription(userId, targetId, invite);
                }
            }
        }
    }

    @Override
    @Transactional
    public void acceptedFriendShip(Long userId, Long friendId) {
        checkingCandidateForFriendship(userId, friendId);
        Optional<Invite> inviteOrEmpty = inviteService.getInvite(userId, friendId);
        if (inviteOrEmpty.isPresent()) {
            acceptedSubscription(userId, friendId, inviteOrEmpty.get());
        } else {
            String message = "Subscription request missing";
            log.debug(message);
            throw new IllegalStateException(message);
        }
    }

    @Override
    @Transactional
    public void rejectedFriendShip(Long userId, Long applicant) {
        checkingCandidateForFriendship(userId, applicant);
        inviteService.deleteInvite(userId, applicant);
    }

    @Override
    @Transactional
    public void deleteFriend(Long userId, Long friendId) {
        checkingCandidateForFriendship(userId, friendId);
        inviteService.deleteInvite(userId, friendId);
        User ownerRequest = getUserById(userId);
        User target = getLinkUser(friendId);
        ownerRequest.removeFriend(target);
        userRepository.save(ownerRequest);
    }

    private void checkingCandidateForFriendship(Long userId, Long friendId) {
        if (Objects.equals(userId, friendId)) {
            String message = "Unacceptable";
            log.debug(message);
            throw new IllegalStateException(message);
        }
    }
    private void acceptedSubscription(Long userId, Long friendId, Invite invite) {
        invite.setStatus(Invite.Status.ACCEPTED);
        User ownerRequest = getUserById(userId);
        User target = getLinkUser(friendId);
        ownerRequest.addFriend(target);
        userRepository.save(ownerRequest);
    }

    private void createSubscription(Long userId, Long friendId) {
        User ownerRequest = getUserById(userId);
        User target = getLinkUser(friendId);
        Invite invite = new Invite();
        invite.setCandidate(friendId);
        invite.setStatus(Invite.Status.CONSIDERATION);
        ownerRequest.addFriend(target);
        ownerRequest.addInvite(invite);
        userRepository.save(ownerRequest);
    }

}
