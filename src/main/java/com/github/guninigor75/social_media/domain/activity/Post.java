package com.github.guninigor75.social_media.domain.activity;

import com.github.guninigor75.social_media.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    private String image;

    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

}
