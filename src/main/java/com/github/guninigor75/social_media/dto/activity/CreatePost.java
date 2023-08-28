package com.github.guninigor75.social_media.dto.activity;

import com.github.guninigor75.social_media.dto.validation.OnCreate;
import com.github.guninigor75.social_media.dto.validation.OnUpdate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreatePost {

    @NotNull(message = "Id must be not null", groups = OnUpdate.class)
    private Long id;

    @NotBlank(message = "Title must be not empty", groups = OnCreate.class)
    @Size(min = 3, max = 50, message = "Length name must be 3 - 50 symbol", groups = OnCreate.class)
    private String title;

    @NotBlank(message = "Content must be not empty",  groups = OnCreate.class)
    @Size(min = 3, max = 255, message = "Length name must be 3 - 255 symbol", groups = OnCreate.class)
    private String content;
}
