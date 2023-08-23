package com.github.guninigor75.social_media.mapper;

import com.github.guninigor75.social_media.dto.activity.CreateMessage;
import com.github.guninigor75.social_media.dto.activity.MessageDto;
import com.github.guninigor75.social_media.entity.activity.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MessageMapper {

    Message createMessageToMessage(CreateMessage createMessage);

    @Mapping(target = "createdAt", expression = "java(message.getCreatedAt().toEpochMilli())")
    MessageDto messageToMessageDto(Message message);

    List<MessageDto> messagesToMessagesDto(List<Message> messages);
}
