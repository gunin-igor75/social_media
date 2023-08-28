package com.github.guninigor75.social_media.dto.auth;

import com.github.guninigor75.social_media.dto.validation.OnCreate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Request for login")
public class JwtRequest {

    @Schema(description = "email", example = "user@gmail.com")
    @NotBlank(message = "Email must be not empty")
    @Email(message = "Email does not match the given form")
    private String username;

    @Schema(description = "password", example = "100")
    @Size(min = 3, max = 20, message = "Length password must be 3 - 255 symbol")
    @NotBlank(message = "Password must be not empty")
    private String password;
}
