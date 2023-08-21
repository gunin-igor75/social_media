package com.github.guninigor75.social_media.dto.auth;

import lombok.Data;

@Data
public class JwtResponse {

    private Long id;

    private String username;

    private String accessToken;

    private String refreshToken;

}