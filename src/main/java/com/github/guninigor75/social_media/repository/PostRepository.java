package com.github.guninigor75.social_media.repository;

import com.github.guninigor75.social_media.entity.activity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("select (count(p) > 0) from Post p where p.id = ?1 and p.user.id = ?2")
    boolean existsByIdAndUser_Id(Long postId, Long userId);

    Page<Post> findByUser_Friends_Id(Long id, Pageable pageable);


    @Query("select count(p) from Post p")
    long count();

}
