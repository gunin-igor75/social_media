package com.github.guninigor75.social_media.repository;

import com.github.guninigor75.social_media.entity.activity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {


    Page<Message> findBySenderId(Long id, Pageable pageable);

    @Query("select m from Message m where m.sender.id = ?1 and m.recipient = ?2")
    Page<Message> findBySender_IdAndRecipient(Long id, Long recipient, Pageable pageable);

}
