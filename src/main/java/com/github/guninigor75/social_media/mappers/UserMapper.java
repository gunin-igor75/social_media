package com.github.guninigor75.social_media.mappers;

import com.github.guninigor75.social_media.entity.user.User;
import com.github.guninigor75.social_media.dto.user.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    User userDtoToUser(UserDto userDto);

    UserDto userToUserDto(User user);
}
