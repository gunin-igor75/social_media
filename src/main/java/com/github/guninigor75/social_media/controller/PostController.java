package com.github.guninigor75.social_media.controller;

import com.github.guninigor75.social_media.dto.activity.CreatePost;
import com.github.guninigor75.social_media.dto.activity.PageDtoPost;
import com.github.guninigor75.social_media.dto.activity.PostDto;
import com.github.guninigor75.social_media.dto.validation.OnCreate;
import com.github.guninigor75.social_media.dto.validation.OnUpdate;
import com.github.guninigor75.social_media.entity.activity.Post;
import com.github.guninigor75.social_media.mapper.PostMapper;
import com.github.guninigor75.social_media.security.SecurityUser;
import com.github.guninigor75.social_media.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
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
@Validated
@Tag(name = "Post Controller", description = "Post Api")
public class PostController {

    private final PostService postService;

    private final PostMapper postMapper;

    @Operation(summary = "Create a post")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "Created"),
                    @ApiResponse(responseCode = "400", description = "Bad request"),
                    @ApiResponse(responseCode = "401", description = "Not found"),
                    @ApiResponse(responseCode = "402", description = "Unauthorized")
            }
    )
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public PostDto createPost(@RequestPart(name = "properties") @Validated(OnCreate.class) CreatePost createPost,
                              @RequestPart(name = "image", required = false) MultipartFile file,
                              @AuthenticationPrincipal SecurityUser securityUser) {
        Post post = postMapper.createPostToPost(createPost);
        Post persistentPost = postService.createPost(post, file, securityUser);
        return postMapper.postToPostDto(persistentPost);
    }

    @Operation(summary = "Post image update")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "400", description = "Bad request"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Not found"),
            }
    )
    @PatchMapping(path = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("@customSecurityExpression.canAccessPost(#id)")
    public PostDto updatePicturePost(@PathVariable @PositiveOrZero Long id,
                                     @RequestPart(name = "image") MultipartFile file) {
        Post post = postService.updatePictureByPostId(id, file);
        return postMapper.postToPostDto(post);
    }

    @Operation(summary = "Post update")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "400", description = "Bad request"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Not found"),
            }
    )
    @PatchMapping(path = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("@customSecurityExpression.canAccessPost(#id)")
    public PostDto updatePost(@RequestBody @Validated(OnUpdate.class) CreatePost createPost,
                              @PathVariable("id") @PositiveOrZero Long id) {
        Post post = postMapper.createPostToPost(createPost);
        Post persistentPost = postService.updatePost(post);
        return postMapper.postToPostDto(persistentPost);
    }

    @Operation(summary = "Deleting a post")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "No content"),
                    @ApiResponse(responseCode = "400", description = "Bad request"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Not found"),
            }
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("@customSecurityExpression.canAccessPost(#id)")
    public void deletePost(@PathVariable Long id) {
        postService.deletePost(id);
    }

    @Operation(summary = "Getting a post")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "Created"),
                    @ApiResponse(responseCode = "400", description = "Bad request"),
                    @ApiResponse(responseCode = "401", description = "Not found"),
                    @ApiResponse(responseCode = "402", description = "Unauthorized")
            }
    )
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PostDto getPost(@PathVariable("id") Long id) {
        Post post = postService.getPostById(id);
        return postMapper.postToPostDto(post);
    }

    @Operation(summary = "Getting a list of posts")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "Created"),
                    @ApiResponse(responseCode = "400", description = "Bad request"),
                    @ApiResponse(responseCode = "401", description = "Not found"),
                    @ApiResponse(responseCode = "402", description = "Unauthorized")
            }
    )
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PostDto> getPosts(@Valid PageDtoPost pageDtoPost) {
        Pageable pageable = new PageDtoPost().getPageable(pageDtoPost);
        List<Post> posts = postService.getPosts(pageable);
        return postMapper.postsToPostsDto(posts);
    }

    @Operation(summary = "Getting a list of friends' posts")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "Created"),
                    @ApiResponse(responseCode = "400", description = "Bad request"),
                    @ApiResponse(responseCode = "401", description = "Not found"),
                    @ApiResponse(responseCode = "402", description = "Unauthorized")
            }
    )
    @GetMapping("/friends")
    @ResponseStatus(HttpStatus.OK)
    public List<PostDto> getPostsFriend(@Valid PageDtoPost pageDtoPost,
                                        @AuthenticationPrincipal SecurityUser securityUser) {
        Pageable pageable = new PageDtoPost().getPageable(pageDtoPost);
        List<Post> posts = postService.getPostsByFriend(securityUser, pageable);
        return postMapper.postsToPostsDto(posts);
    }
}
