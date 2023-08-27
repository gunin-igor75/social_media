package com.github.guninigor75.social_media.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Request for login")
public class JwtRequest {

    @Schema(description = "email", example = "user@gmail.com")
    @NotBlank(message = "Username must be not empty")
    private String username;

    @Schema(description = "password", example = "100")
    @NotBlank(message = "Password must be not empty")
    private String password;
}
