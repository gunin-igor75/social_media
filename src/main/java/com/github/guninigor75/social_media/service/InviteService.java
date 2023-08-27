package com.github.guninigor75.social_media.service;

import com.github.guninigor75.social_media.entity.user.Invite;

import java.util.Optional;

public interface InviteService {
    Optional<Invite> getInvite(Long userId, Long candidate);

    void checkFriendship(Long userId, Long friend);

    void deleteInvite(Long userId, Long applicant);
}
