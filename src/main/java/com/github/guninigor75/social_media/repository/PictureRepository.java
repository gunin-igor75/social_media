package com.github.guninigor75.social_media.repository;

import com.github.guninigor75.social_media.entity.activity.Picture;
import com.github.guninigor75.social_media.entity.activity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface PictureRepository extends JpaRepository<Picture, Integer> {
    Optional<Picture> findByPost_id(Long id);

    void deleteByPost_id(Long id);

}
