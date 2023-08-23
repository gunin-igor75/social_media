package com.github.guninigor75.social_media.service.imp;

import com.github.guninigor75.social_media.entity.user.Friend;
import com.github.guninigor75.social_media.repository.FriendRepository;
import com.github.guninigor75.social_media.service.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FriendServiceImp implements FriendService {

    private final FriendRepository friendRepository;

    @Override
    public void createFriend(Friend friend) {
        friendRepository.save(friend);
    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {
        friendRepository.deleteByUserIdAndFriendId(userId, friendId);
    }

    @Override
    public boolean isFriend(Long userId, Long friendId) {
        return friendRepository.existsByUserIdAndFriendId(userId, friendId);
    }
}
