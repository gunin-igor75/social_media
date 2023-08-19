package com.github.guninigor75.social_media.service;

import com.github.guninigor75.social_media.web.dto.auth.JwtRequest;
import com.github.guninigor75.social_media.web.dto.auth.JwtResponse;

public interface AuthService {
    JwtResponse login(JwtRequest loginRequest);

    JwtResponse refresh(String refreshToken);
}
