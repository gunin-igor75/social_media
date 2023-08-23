package com.github.guninigor75.social_media.repository;

import com.github.guninigor75.social_media.entity.user.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {


    void deleteByUserIdAndFriendId(Long userId, Long friendId);

    @Query("select (count(f) > 0) from Friend f where f.user.id = ?1 and f.friend.id = ?2")
    boolean existsByUserIdAndFriendId(Long userId, Long friendId);

}
