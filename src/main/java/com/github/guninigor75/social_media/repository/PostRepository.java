package com.github.guninigor75.social_media.repository;

import com.github.guninigor75.social_media.entity.activity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("select (count(p) > 0) from Post p where p.id = ?1 and p.user.id = ?2")
    boolean existsByIdAndUser_Id(Long postId, Long userId);

    @Query(value = "select p from Post p where p.user.id in (select  f.friend.id from Friend f where f.user.id = ?1)")
    Page<Post> findByFriend(Long id, Pageable pageable);

}
