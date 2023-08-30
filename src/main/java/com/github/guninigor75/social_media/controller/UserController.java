package com.github.guninigor75.social_media.controller;

import com.github.guninigor75.social_media.dto.user.UpdateUser;
import com.github.guninigor75.social_media.dto.user.UserDto;
import com.github.guninigor75.social_media.entity.user.User;
import com.github.guninigor75.social_media.mapper.UserMapper;
import com.github.guninigor75.social_media.security.SecurityUser;
import com.github.guninigor75.social_media.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "User Controller", description = "User Api")
public class UserController {

    private final UserService userService;

    private final UserMapper userMapper;

    @Operation(summary = "User update")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "400", description = "Bad request"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "404", description = "Not found"),
            }
    )
    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public UserDto update(@RequestBody @Valid UpdateUser updateUser,
                          @AuthenticationPrincipal SecurityUser securityUser) {
        User user = userService.updateUser(securityUser.getId(), updateUser);
        return userMapper.userToUserDto(user);
    }
}
