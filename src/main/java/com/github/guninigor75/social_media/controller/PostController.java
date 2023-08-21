package com.github.guninigor75.social_media.controller;

import com.github.guninigor75.social_media.dto.activity.CreatePost;
import com.github.guninigor75.social_media.dto.activity.PagePostsDto;
import com.github.guninigor75.social_media.dto.activity.PostDto;
import com.github.guninigor75.social_media.dto.validation.OnCreate;
import com.github.guninigor75.social_media.dto.validation.OnUpdate;
import com.github.guninigor75.social_media.entity.activity.Post;
import com.github.guninigor75.social_media.mappers.PostMapper;
import com.github.guninigor75.social_media.security.SecurityUser;
import com.github.guninigor75.social_media.service.PostService;
import com.github.guninigor75.social_media.util.FileManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    private final FileManager fileManager;

    private final PostMapper postMapper;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public PostDto createPost(@RequestPart(name = "properties") @Validated(OnCreate.class) CreatePost createPost,
                              @RequestPart(name = "image") MultipartFile file,
                              @AuthenticationPrincipal SecurityUser securityUser) {
        fileManager.checkFile(file);
        Post post = postMapper.createPostToPost(createPost);
        Post persistentPost = postService.createPost(post, file, securityUser);
        return postMapper.postToPostDto(persistentPost);
    }

    @PatchMapping(path = "{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("@customSecurityExpression.canAccessPost(#id)")
    public PostDto updatePicturePost(@PathVariable Long id,
                                     @RequestPart(name = "image") MultipartFile file) {
        fileManager.checkFile(file);
        Post post = postService.updatePictureByPostId(id, file);
        return postMapper.postToPostDto(post);
    }

    @PatchMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("@customSecurityExpression.canAccessPost(#id)")
    public PostDto updatePost(@RequestBody @Validated(OnUpdate.class) CreatePost createPost) {
        Post post = postService.updatePost(createPost);
        return postMapper.postToPostDto(post);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("@customSecurityExpression.canAccessPost(#id)")
    public void deleteAds(@PathVariable Long id) {
        postService.deletePost(id);
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public PostDto getPost(@PathVariable("id") Long id) {
        Post post = postService.getPost(id);
        return postMapper.postToPostDto(post);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PostDto> getPosts(PagePostsDto pagePostsDto) {
        Pageable pageable = new PagePostsDto().getPageable(pagePostsDto);
        List<Post> posts = postService.getPosts(pageable);
        return postMapper.postsToPostsDto(posts);
    }
}
