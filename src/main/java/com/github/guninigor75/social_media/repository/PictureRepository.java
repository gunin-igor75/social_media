package com.github.guninigor75.social_media.repository;

import com.github.guninigor75.social_media.entity.activity.Picture;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PictureRepository extends JpaRepository<Picture, Integer> {
    @EntityGraph(attributePaths = "post")
    Optional<Picture> findByPost_id(Long id);

    void deleteByPost_id(Long id);

    @Query("select count(p) from Picture p")
    long count();
}
