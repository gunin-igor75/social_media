package com.github.guninigor75.social_media.repository;

import com.github.guninigor75.social_media.entity.user.Invite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InviteRepository extends JpaRepository<Invite, Long> {


    @Query("select i from Invite i where i.user.id = ?1 and i.candidate = ?2 or i.candidate = ?1 and i.user.id = ?2")
    Optional<Invite> findInvate(Long id, Long candidate);

    @Query("select (count(i) > 0) from Invite i where (i.user.id = ?1 and i.candidate = ?2) or (i.candidate = ?1 and i.user.id = ?2)")
    boolean checkFriendship(Long id, Long friend) ;

}
