package com.github.guninigor75.social_media.service;

import com.github.guninigor75.social_media.entity.activity.Message;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MessageService {

    Message createMessage(Long userId, Long friendId, Message message);

    List<Message> getMessages(Long senderId, Pageable pageable);

    List<Message> getMessagesUser(Long senderId, Long recipientId, Pageable pageable);
}
