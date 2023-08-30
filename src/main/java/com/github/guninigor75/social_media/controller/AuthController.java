package com.github.guninigor75.social_media.controller;

import com.github.guninigor75.social_media.entity.user.User;
import com.github.guninigor75.social_media.service.AuthService;
import com.github.guninigor75.social_media.service.UserService;
import com.github.guninigor75.social_media.dto.auth.JwtRequest;
import com.github.guninigor75.social_media.dto.auth.JwtResponse;
import com.github.guninigor75.social_media.dto.user.UserDto;
import com.github.guninigor75.social_media.dto.validation.OnCreate;
import com.github.guninigor75.social_media.mapper.UserMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Auth Controller", description = "Auth API")
public class AuthController {

    private final AuthService authService;

    private final UserService userService;

    private final UserMapper userMapper;

    @Operation(summary = "User authorization")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "400", description = "Bad request"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            }
    )
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public JwtResponse login(@RequestBody @Valid JwtRequest jwtRequest) {
        return authService.login(jwtRequest);
    }

    @Operation(summary = "User registration")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "Created"),
                    @ApiResponse(responseCode = "400", description = "Bad request")
            }
    )
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto register(@RequestBody @Validated(OnCreate.class)
                            UserDto userDto) {
        User user = userMapper.userDtoToUser(userDto);
        User persistentUser = userService.createUser(user);
        return userMapper.userToUserDto(persistentUser);
    }

    @Operation(summary = "Tokens refresh")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized")
            }
    )
    @PostMapping("/refresh")
    @ResponseStatus(HttpStatus.OK)
    public JwtResponse refresh(@RequestBody String refreshToken) {
        return authService.refresh(refreshToken);
    }
}
