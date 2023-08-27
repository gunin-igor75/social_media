package com.github.guninigor75.social_media.service.imp;

import com.github.guninigor75.social_media.entity.user.Invite;
import com.github.guninigor75.social_media.exception_handler.ResourceNotFoundException;
import com.github.guninigor75.social_media.repository.InviteRepository;
import com.github.guninigor75.social_media.service.InviteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class InviteServiceImpl implements InviteService {

    private final InviteRepository inviteRepository;

    @Override
    public Optional<Invite> getInvite(Long userId, Long candidate) {
        return inviteRepository.findInvate(userId, candidate);
    }

    @Override
    public void checkFriendship(Long userId, Long friend) {
        boolean check = inviteRepository.checkFriendship(userId, friend);
        if (!check) {
            String message = "Correspondence is available only to friends";
            log.debug(message);
            throw new IllegalStateException(message);
        }
    }

    @Override
    public void deleteInvite(Long userId, Long applicant) {
        Invite invite = inviteRepository.findInvate(userId, applicant).orElseThrow(
                () -> {
                    String message = String.format("Application with %d  %d does not exist", userId, applicant);
                    log.debug(message);
                    return new ResourceNotFoundException(message);
                }
        );
        inviteRepository.delete(invite);
    }
}
