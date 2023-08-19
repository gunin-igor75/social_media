package com.github.guninigor75.social_media.service.imp;

import com.github.guninigor75.social_media.service.AuthService;
import com.github.guninigor75.social_media.web.dto.auth.JwtRequest;
import com.github.guninigor75.social_media.web.dto.auth.JwtResponse;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImp implements AuthService {
    @Override
    public JwtResponse login(JwtRequest loginRequest) {
        return null;
    }

    @Override
    public JwtResponse refresh(String refreshToken) {
        return null;
    }
}
