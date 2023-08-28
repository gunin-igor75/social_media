package com.github.guninigor75.social_media.controller;

import com.github.guninigor75.social_media.dto.activity.CreateMessage;
import com.github.guninigor75.social_media.dto.activity.MessageDto;
import com.github.guninigor75.social_media.dto.activity.PageDto;
import com.github.guninigor75.social_media.entity.activity.Message;
import com.github.guninigor75.social_media.mapper.MessageMapper;
import com.github.guninigor75.social_media.security.SecurityUser;
import com.github.guninigor75.social_media.service.MessageService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/messages")
@RequiredArgsConstructor
@Validated
public class MessageController {

    private final MessageService messageService;

    private final MessageMapper messageMapper;

    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public MessageDto createMessage(@RequestBody @Valid CreateMessage createMessage,
                                    @PathVariable("id") @PositiveOrZero Long friendId,
                                    @AuthenticationPrincipal SecurityUser securityUser) {
        Message message = messageMapper.createMessageToMessage(createMessage);
        Message persistentMessage = messageService.createMessage(securityUser.getId(), friendId, message);
        return messageMapper.messageToMessageDto(persistentMessage);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<MessageDto> getMessages(PageDto pageDto,
                                        @AuthenticationPrincipal SecurityUser securityUser) {
        Pageable pageable = new PageDto().getPageable(pageDto);
        List<Message> messages = messageService.getMessages(securityUser.getId(), pageable);
        return messageMapper.messagesToMessagesDto(messages);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<MessageDto> getMessagesUser(PageDto pageDto,
                                            @PathVariable("id") @PositiveOrZero Long recipientId,
                                            @AuthenticationPrincipal SecurityUser securityUser) {
        Pageable pageable = new PageDto().getPageable(pageDto);
        List<Message> messages = messageService.getMessagesUser(securityUser.getId(), recipientId, pageable);
        return messageMapper.messagesToMessagesDto(messages);
    }
}
