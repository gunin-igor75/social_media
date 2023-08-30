package com.github.guninigor75.social_media.service.imp;

import com.github.guninigor75.social_media.entity.activity.Message;
import com.github.guninigor75.social_media.entity.user.User;
import com.github.guninigor75.social_media.repository.MessageRepository;
import com.github.guninigor75.social_media.service.InviteService;
import com.github.guninigor75.social_media.service.MessageService;
import com.github.guninigor75.social_media.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;

    private final UserService userService;

    private final InviteService inviteService;


    @Override
    @Transactional
    public Message createMessage(Long userId, Long friendId, Message message) {
        inviteService.checkFriendship(userId, friendId);
        User sender = userService.getLinkUser(userId);
        message.setSender(sender);
        message.setRecipient(friendId);
        return messageRepository.save(message);
    }

    @Override
    public List<Message> getMessages(Long senderId, Pageable pageable) {
        Page<Message> all = messageRepository.findBySenderId(senderId, pageable);
        return all.getContent();
    }

    @Override
    public List<Message> getMessagesUser(Long senderId, Long recipientId, Pageable pageable) {
        Page<Message> all = messageRepository.findBySender_IdAndRecipient(senderId, recipientId, pageable);
        return all.getContent();
    }
}
