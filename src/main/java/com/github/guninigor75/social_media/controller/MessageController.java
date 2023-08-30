package com.github.guninigor75.social_media.controller;

import com.github.guninigor75.social_media.dto.activity.CreateMessage;
import com.github.guninigor75.social_media.dto.activity.MessageDto;
import com.github.guninigor75.social_media.dto.activity.PageDtoMessage;
import com.github.guninigor75.social_media.entity.activity.Message;
import com.github.guninigor75.social_media.mapper.MessageMapper;
import com.github.guninigor75.social_media.security.SecurityUser;
import com.github.guninigor75.social_media.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Messages Controller", description = "Messages Api")
public class MessageController {

    private final MessageService messageService;

    private final MessageMapper messageMapper;

    @Operation(summary = "Create a message")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "Created"),
                    @ApiResponse(responseCode = "400", description = "Bad request"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            }
    )
    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public MessageDto createMessage(@RequestBody @Valid CreateMessage createMessage,
                                    @PathVariable("id") @PositiveOrZero Long friendId,
                                    @AuthenticationPrincipal SecurityUser securityUser) {
        Message message = messageMapper.createMessageToMessage(createMessage);
        Message persistentMessage = messageService.createMessage(securityUser.getId(), friendId, message);
        return messageMapper.messageToMessageDto(persistentMessage);
    }

    @Operation(summary = "Receiving a message")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "400", description = "Bad request"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            }
    )
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<MessageDto> getMessages(@Valid PageDtoMessage pageDtoMessage,
                                        @AuthenticationPrincipal SecurityUser securityUser) {
        Pageable pageable = new PageDtoMessage().getPageable(pageDtoMessage);
        List<Message> messages = messageService.getMessages(securityUser.getId(), pageable);
        return messageMapper.messagesToMessagesDto(messages);
    }

    @Operation(summary = "Getting a message list")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "400", description = "Bad request"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            }
    )
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<MessageDto> getMessagesUser(@Valid PageDtoMessage pageDtoMessage,
                                            @PathVariable("id") @PositiveOrZero Long recipientId,
                                            @AuthenticationPrincipal SecurityUser securityUser) {
        Pageable pageable = new PageDtoMessage().getPageable(pageDtoMessage);
        List<Message> messages = messageService.getMessagesUser(securityUser.getId(), recipientId, pageable);
        return messageMapper.messagesToMessagesDto(messages);
    }
}
