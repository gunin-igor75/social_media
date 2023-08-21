package com.github.guninigor75.social_media.dto.activity;

import lombok.Data;

import java.time.Instant;

@Data
public class PostDto {

    private Long id;

    private String title;

    private String content;

    private String image;

    private Instant createdAt;

    private Instant updatedAt;

}
