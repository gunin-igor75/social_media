package com.github.guninigor75.social_media.repository;

import com.github.guninigor75.social_media.entity.user.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    @EntityGraph(attributePaths = "roles")
    Optional<User> findByUsername(String username);


    @EntityGraph(attributePaths = {"friends"})
    Optional<User> findById(Long id);

}
