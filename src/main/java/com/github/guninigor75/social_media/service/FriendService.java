package com.github.guninigor75.social_media.service;

import com.github.guninigor75.social_media.entity.user.Friend;

public interface FriendService {
    void createFriend(Friend friend);

    void deleteFriend(Long userId, Long friendId);

    boolean isFriend(Long userId, Long friendId);
}
