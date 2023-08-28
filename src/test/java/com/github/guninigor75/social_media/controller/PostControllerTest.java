package com.github.guninigor75.social_media.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.guninigor75.social_media.config.IntegrationSuite;
import com.github.guninigor75.social_media.dto.activity.CreatePost;
import com.github.guninigor75.social_media.dto.activity.PostDto;
import com.github.guninigor75.social_media.entity.activity.Post;
import com.github.guninigor75.social_media.entity.user.User;
import com.github.guninigor75.social_media.mapper.PostMapper;
import com.github.guninigor75.social_media.repository.UserRepository;
import com.github.guninigor75.social_media.security.JwtTokenProvider;
import com.github.guninigor75.social_media.security.SecurityUser;
import com.github.guninigor75.social_media.service.PostService;
import com.github.guninigor75.social_media.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.Optional;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
@ExtendWith(MockitoExtension.class)
class PostControllerTest extends IntegrationSuite {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private PostService postService;

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    static final String TOKEN_PREFIX = "Bearer";

    @BeforeEach
    public void init() {
        userRepository.deleteAll();
        ;
    }

    @Test
    void createPostPositiveTest() throws Exception {
        User user = userService.createUser(getUser());
        Long userId = user.getId();
        String token = jwtTokenProvider.createAccessToken(user);
        CreatePost createPost = givenCreatePost();

        byte[] bytesImage = "Post created".getBytes();
        byte[] bytesCreatePost = objectMapper.writeValueAsBytes(createPost);

        MockPart partFile = new MockPart("image", "image.jpg", bytesImage);
        partFile.getHeaders().setContentType(MediaType.IMAGE_JPEG);

        MockPart partCreatePost = new MockPart("properties", "createPost", bytesCreatePost);
        partCreatePost.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        Post post = givenPost(createPost, user);
        PostDto postDto = postMapper.postToPostDto(post);


        when(postService.createPost(any(Post.class), any(MultipartFile.class), any(SecurityUser.class)))
                .thenReturn(post);

        mvc.perform(MockMvcRequestBuilders
                        .multipart(HttpMethod.POST, "/api/v1/posts")
                        .part(partCreatePost)
                        .part(partFile)
                        .header("Authorization", TOKEN_PREFIX + " " + token))
                .andDo(print())
                .andExpect(jsonPath("$.id").value(postDto.getId()))
                .andExpect(jsonPath("$.title").value(postDto.getTitle()))
                .andExpect(jsonPath("$.content").value(postDto.getContent()))
                .andExpect(jsonPath("$.createdAt").value(postDto.getCreatedAt()))
                .andExpect(jsonPath("$.updatedAt").value(postDto.getUpdatedAt()))
                .andExpect(jsonPath("$.image").value(postDto.getImage()))
                .andExpect(status().is(201));
    }

    @ParameterizedTest
    @MethodSource("argsProviderFactoryCreateDto")
    void createPostNegativeTest(String title) throws Exception {
        User user = userService.createUser(getUser());
        String token = jwtTokenProvider.createAccessToken(user);
        CreatePost createPost = givenCreatePostTitle();
        createPost.setTitle(title);

        byte[] bytesImage = "Post created".getBytes();
        byte[] bytesCreatePost = objectMapper.writeValueAsBytes(createPost);

        MockPart partFile = new MockPart("image", "image.jpg", bytesImage);
        partFile.getHeaders().setContentType(MediaType.IMAGE_JPEG);

        MockPart partCreatePost = new MockPart("properties", "createPost", bytesCreatePost);
        partCreatePost.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        mvc.perform(MockMvcRequestBuilders
                        .multipart(HttpMethod.POST, "/api/v1/posts")
                        .part(partCreatePost)
                        .part(partFile)
                        .header("Authorization", TOKEN_PREFIX + " " + token))
                .andDo(print())
                .andExpect(status().is(400));
    }

    @ParameterizedTest
    @MethodSource("argsProviderFactoryCreateDto")
    void createPostNegativeContentTest(String content) throws Exception {
        User user = userService.createUser(getUser());
        String token = jwtTokenProvider.createAccessToken(user);
        CreatePost createPost = givenCreatePostTContent();
        createPost.setContent(content);

        byte[] bytesImage = "Post created".getBytes();
        byte[] bytesCreatePost = objectMapper.writeValueAsBytes(createPost);

        MockPart partFile = new MockPart("image", "image.jpg", bytesImage);
        partFile.getHeaders().setContentType(MediaType.IMAGE_JPEG);

        MockPart partCreatePost = new MockPart("properties", "createPost", bytesCreatePost);
        partCreatePost.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        mvc.perform(MockMvcRequestBuilders
                        .multipart(HttpMethod.POST, "/api/v1/posts")
                        .part(partCreatePost)
                        .part(partFile)
                        .header("Authorization", TOKEN_PREFIX + " " + token))
                .andDo(print())
                .andExpect(status().is(400));
    }

    @Test
    void updatePicturePostPositiveTest() throws Exception {
        User user = userService.createUser(getUser());
        String token = jwtTokenProvider.createAccessToken(user);
        CreatePost createPost = givenCreatePost();
        long postId = 10L;

        byte[] bytes = "test PostController".getBytes();
        MockMultipartFile mockFile =
                new MockMultipartFile("image", "image.jpg", String.valueOf(MediaType.IMAGE_JPEG), bytes);

        Post post = givenPost(createPost, user);
        PostDto postDto = postMapper.postToPostDto(post);


        when(postService.updatePictureByPostId(any(Long.class), any(MultipartFile.class)))
                .thenReturn(post);


        mvc.perform(MockMvcRequestBuilders
                        .multipart(HttpMethod.PATCH, "/api/v1/posts/{id}/image", postId)
                        .file(mockFile)
                        .header("Authorization", TOKEN_PREFIX + " " + token))
                .andDo(print())
                .andExpect(jsonPath("$.id").value(postDto.getId()))
                .andExpect(jsonPath("$.title").value(postDto.getTitle()))
                .andExpect(jsonPath("$.content").value(postDto.getContent()))
                .andExpect(jsonPath("$.createdAt").value(postDto.getCreatedAt()))
                .andExpect(jsonPath("$.updatedAt").value(postDto.getUpdatedAt()))
                .andExpect(jsonPath("$.image").value(postDto.getImage()))
                .andExpect(status().is(200));
    }


    private User getUser() {
        User user = new User();
        user.setName("user");
        user.setUsername("user@gmail.ru");
        user.setPassword("100");
        return user;
    }

    private CreatePost givenCreatePost() {
        CreatePost createPost = new CreatePost();
        createPost.setTitle("Post 1");
        createPost.setContent("Content post 1");
        return createPost;
    }

    private Post givenPost(CreatePost createPost, User user) {
        Post post = new Post();
        post.setId(1L);
        post.setTitle(createPost.getTitle());
        post.setUser(user);
        post.setContent(createPost.getContent());
        post.setCreatedAt(Instant.now());
        post.setUpdatedAt(Instant.now());
        post.setImage("image.jpg");
        return post;
    }

    private CreatePost givenCreatePostTitle() {
        CreatePost createPost = new CreatePost();
        createPost.setContent("Content Post");
        return createPost;
    }

    private CreatePost givenCreatePostTContent() {
        CreatePost createPost = new CreatePost();
        createPost.setTitle("Title Post");
        return createPost;
    }

    public static Stream<String> argsProviderFactoryCreateDto() {
        return Stream.of("", "sw");
    }
}