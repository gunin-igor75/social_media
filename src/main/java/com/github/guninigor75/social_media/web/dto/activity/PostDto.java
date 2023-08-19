package com.github.guninigor75.social_media.web.dto.activity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostDto {

    private Long id;

    private String title;

    private String content;

    private String image;

    private LocalDateTime createdAt;

}
