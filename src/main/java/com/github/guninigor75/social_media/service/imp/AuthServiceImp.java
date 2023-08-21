package com.github.guninigor75.social_media.service.imp;

import com.github.guninigor75.social_media.entity.user.User;
import com.github.guninigor75.social_media.service.AuthService;
import com.github.guninigor75.social_media.service.UserService;
import com.github.guninigor75.social_media.dto.auth.JwtRequest;
import com.github.guninigor75.social_media.dto.auth.JwtResponse;
import com.github.guninigor75.social_media.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImp implements AuthService {

    private final UserService userService;

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public JwtResponse login(JwtRequest loginRequest) {
        JwtResponse jwtResponse = new JwtResponse();
        User user = userService.getByUsername(loginRequest.getUsername());
        jwtResponse.setId(user.getId());
        jwtResponse.setUsername(user.getUsername());
        jwtResponse.setAccessToken(jwtTokenProvider.createAccessToken(user));
        jwtResponse.setRefreshToken(jwtTokenProvider.createRefreshToken(user));
        return jwtResponse;
    }

    @Override
    public JwtResponse refresh(String refreshToken) {
        String username = jwtTokenProvider.getUsername(refreshToken);
        User user = userService.getByUsername(username);
        return jwtTokenProvider.updateUserTokens(user);
    }
}
