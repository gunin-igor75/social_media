package com.github.guninigor75.social_media.service;

import com.github.guninigor75.social_media.dto.activity.CreatePost;
import com.github.guninigor75.social_media.entity.activity.Post;
import com.github.guninigor75.social_media.security.SecurityUser;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {
    Post createPost(Post post, MultipartFile file, SecurityUser securityUser);

    void deletePost(Long id);

    Post updatePictureByPostId(Long id, MultipartFile file);

    Post updatePost(CreatePost createPost);

    List<Post> getPosts(Pageable pageable);

    List<Post> getPostsByFriend(SecurityUser securityUser, Pageable pageable);

    boolean isOwnerPost(Long postId, Long id);

    Post getPostById(Long id);
}
