package com.github.guninigor75.social_media.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.guninigor75.social_media.dto.validation.OnCreate;
import com.github.guninigor75.social_media.dto.validation.OnUpdate;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDto {

    @NotNull(message = "Id must be not null", groups = OnUpdate.class)
    private Long id;

    @NotBlank(message = "Name must be not empty", groups = OnCreate.class)
    @Size(min = 3, max = 20, message = "Length name must be 3 - 255 symbol", groups = OnCreate.class)
    private String name;

    @NotBlank(message = "Email must be not empty", groups = OnCreate.class)
    @Email(message = "Email does not match the given form", groups = OnCreate.class)
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Size(min = 3, max = 20, message = "Length password must be 3 - 255 symbol", groups = OnCreate.class)
    @NotBlank(message = "Password must be not empty", groups = OnCreate.class)
    private String password;
}
