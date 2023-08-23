package com.github.guninigor75.social_media.controller;

import com.github.guninigor75.social_media.dto.activity.CreateMessage;
import com.github.guninigor75.social_media.dto.activity.MessageDto;
import com.github.guninigor75.social_media.dto.activity.PageDto;
import com.github.guninigor75.social_media.entity.activity.Message;
import com.github.guninigor75.social_media.mapper.MessageMapper;
import com.github.guninigor75.social_media.security.SecurityUser;
import com.github.guninigor75.social_media.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final MessageMapper messageMapper;

    @PostMapping("/friends/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void requestFriendship(@PathVariable("id")@PositiveOrZero Long id,
                                  @AuthenticationPrincipal SecurityUser securityUser) {
        userService.createFriendship(securityUser, id);
    }

    @PatchMapping("/friends/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void requestAcceptFriendship(@PathVariable("id") @PositiveOrZero Long id,
                                  @AuthenticationPrincipal SecurityUser securityUser) {
        userService.createFriendship(securityUser, id);
    }

    @DeleteMapping("/friends/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void requestDeleteFriend(@PathVariable("id") @PositiveOrZero Long id,
                                    @AuthenticationPrincipal SecurityUser securityUser) {
        userService.deleteFriend(securityUser, id);
    }

    @PostMapping("/messages/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public MessageDto createMessage(@RequestBody @Valid CreateMessage createMessage,
                                    @PathVariable("id") @PositiveOrZero Long friendId,
                                    @AuthenticationPrincipal SecurityUser securityUser) {
        Message message = messageMapper.createMessageToMessage(createMessage);
        Message persistentMessage = userService.createMessage(securityUser, friendId, message);
        return messageMapper.messageToMessageDto(persistentMessage);
    }

    @GetMapping("/messages")
    @ResponseStatus(HttpStatus.OK)
    public List<MessageDto> getMessages(PageDto pageDto,
                                        @AuthenticationPrincipal SecurityUser securityUser) {
        Pageable pageable = new PageDto().getPageable(pageDto);
        List<Message> messages = userService.getMessages(securityUser, pageable);
        return messageMapper.messagesToMessagesDto(messages);
    }

    @GetMapping("/messages/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<MessageDto> getMessagesUser(PageDto pageDto,
                                        @PathVariable("id") @PositiveOrZero Long recipientId,
                                        @AuthenticationPrincipal SecurityUser securityUser) {
        Pageable pageable = new PageDto().getPageable(pageDto);
        List<Message> messages = userService.getMessagesUser(securityUser, recipientId, pageable);
        return messageMapper.messagesToMessagesDto(messages);
    }

}
