package com.github.guninigor75.social_media.service.imp;

import com.github.guninigor75.social_media.entity.activity.Message;
import com.github.guninigor75.social_media.repository.MessageRepository;
import com.github.guninigor75.social_media.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;

    @Override
    public Message createMessage(Message message) {
        return messageRepository.save(message);
    }

    @Override
    public List<Message> getMessages(Long senderId, Pageable pageable) {
        Page<Message> all = messageRepository.findBySenderId(senderId, pageable);
        return all.getContent();
    }

    @Override
    public List<Message> getMessagesUser(Long senderId, Long recipientId, Pageable pageable) {
        Page<Message> all = messageRepository.findBySenderIdAndRecipientId(senderId, recipientId, pageable);
        return all.getContent();
    }
}
