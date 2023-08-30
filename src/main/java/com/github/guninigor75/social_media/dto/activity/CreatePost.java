package com.github.guninigor75.social_media.dto.activity;

import com.github.guninigor75.social_media.dto.validation.OnCreate;
import com.github.guninigor75.social_media.dto.validation.OnUpdate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Request for PostController")
public class CreatePost {

    @Schema(description = "id", example = "1")
    @NotNull(message = "Id must be not null", groups = OnUpdate.class)
    private Long id;

    @Schema(description = "title", example = "title post")
    @NotBlank(message = "Title must be not empty", groups = OnCreate.class)
    @Size(min = 3, max = 50, message = "Length name must be 3 - 50 symbol", groups = {OnCreate.class, OnUpdate.class})
    private String title;

    @Schema(description = "content", example = "post content")
    @NotBlank(message = "Content must be not empty",  groups = OnCreate.class)
    @Size(min = 3, max = 255, message = "Length name must be 3 - 255 symbol", groups = {OnCreate.class, OnUpdate.class})
    private String content;
}
