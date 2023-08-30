package com.github.guninigor75.social_media.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Request for UserController")
public class UpdateUser {

    @Schema(description = "name", example = "user")
    @Size(min = 3, max = 20, message = "Length name must be 3 - 20 symbol")
    private String name;

    @Schema(description = "password", example = "100")
    @Size(min = 3, max = 20, message = "Length password must be 3 - 20 symbol")
    @Pattern(regexp = "^\\s*(\\d\\s*){3,20}$", message = "There must be no whitespace characters")
    private String password;
}
