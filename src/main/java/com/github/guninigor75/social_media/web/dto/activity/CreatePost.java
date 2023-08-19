package com.github.guninigor75.social_media.web.dto.activity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreatePost {

    @NotBlank(message = "Title must be not empty")
    @Size(min = 3, max = 50, message = "Length name must be 3 - 255 symbol")
    private String title;

    @NotBlank(message = "Content must be not empty")
    @Size(min = 3, max = 255, message = "Length name must be 3 - 255 symbol")
    private String content;
}
