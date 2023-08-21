package com.github.guninigor75.social_media.controller;

import com.github.guninigor75.social_media.entity.user.User;
import com.github.guninigor75.social_media.service.AuthService;
import com.github.guninigor75.social_media.service.UserService;
import com.github.guninigor75.social_media.dto.auth.JwtRequest;
import com.github.guninigor75.social_media.dto.auth.JwtResponse;
import com.github.guninigor75.social_media.dto.user.UserDto;
import com.github.guninigor75.social_media.dto.validation.OnCreate;
import com.github.guninigor75.social_media.mappers.UserMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private final UserService userService;

    private final UserMapper userMapper;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public JwtResponse login(@Valid @RequestBody JwtRequest jwtRequest) {
        return authService.login(jwtRequest);
    }


    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto register(@Validated(OnCreate.class) UserDto userDto) {
        User user = userMapper.userDtoToUser(userDto);
        User persistentUser = userService.createUser(user);
        return userMapper.userToUserDto(persistentUser);
    }

    @PostMapping("/refresh")
    @ResponseStatus(HttpStatus.OK)
    public JwtResponse refresh(@RequestBody String refreshToken) {
        return authService.refresh(refreshToken);
    }
}
