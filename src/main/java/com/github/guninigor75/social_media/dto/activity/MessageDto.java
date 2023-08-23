package com.github.guninigor75.social_media.dto.activity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MessageDto {

    private Long id;

    private String content;

    private Long createdAt;

}
