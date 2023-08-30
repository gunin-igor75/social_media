package com.github.guninigor75.social_media.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.guninigor75.social_media.config.IntegrationSuite;
import com.github.guninigor75.social_media.dto.activity.CreatePost;
import com.github.guninigor75.social_media.dto.activity.PostDto;
import com.github.guninigor75.social_media.entity.activity.Post;
import com.github.guninigor75.social_media.entity.user.User;
import com.github.guninigor75.social_media.mapper.PostMapper;
import com.github.guninigor75.social_media.repository.PictureRepository;
import com.github.guninigor75.social_media.repository.PostRepository;
import com.github.guninigor75.social_media.repository.UserRepository;
import com.github.guninigor75.social_media.security.JwtTokenProvider;
import com.github.guninigor75.social_media.security.SecurityUser;
import com.github.guninigor75.social_media.service.FileManagerService;
import com.github.guninigor75.social_media.service.PostService;
import com.github.guninigor75.social_media.service.UserService;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
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

    @SpyBean
    private PostService postService;

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PictureRepository pictureRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private FileManagerService fileManagerService;

    static final String TOKEN_PREFIX = "Bearer";

    @BeforeEach
    public void init() {
        pictureRepository.deleteAll();
        postRepository.deleteAll();
        userRepository.deleteAll();
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

        doReturn(post).when(postService).createPost(any(Post.class), any(MultipartFile.class), any(SecurityUser.class));

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
        SecurityUser securityUser = givenSecurityUser(user);
        CreatePost createPost = givenCreatePost();
        Post post = postMapper.createPostToPost(createPost);
        Post persistentPost = postService.createPost(post, null, securityUser);
        long postId = persistentPost.getId();

        byte[] bytes = "test PostController".getBytes();
        MockMultipartFile mockFile =
                new MockMultipartFile("image", "image.jpg", String.valueOf(MediaType.IMAGE_JPEG), bytes);

        mvc.perform(MockMvcRequestBuilders
                        .multipart(HttpMethod.PATCH, "/api/v1/posts/{id}/image", postId)
                        .file(mockFile)
                        .header("Authorization", TOKEN_PREFIX + " " + token))
                .andDo(print())
                .andExpect(jsonPath("$.id").value(persistentPost.getId()))
                .andExpect(jsonPath("$.title").value(persistentPost.getTitle()))
                .andExpect(jsonPath("$.content").value(persistentPost.getContent()))
                .andExpect(jsonPath("$.createdAt").value(persistentPost.getCreatedAt().toEpochMilli()))
                .andExpect(status().is(200));

        Post postById = postService.getPostById(postId);
        fileManagerService.deleteFile(postById.getImage());
    }

    @Test
    void updatePicturePostNegativeStatus403Test() throws Exception {
        User user = userService.createUser(getUser());
        SecurityUser securityUser = givenSecurityUser(user);
        CreatePost createPost = givenCreatePost();
        Post post = postMapper.createPostToPost(createPost);
        Post persistentPost = postService.createPost(post, null, securityUser);
        long postId = persistentPost.getId();

        User friend = userService.createUser(getFriend());
        String tokenFriend = jwtTokenProvider.createAccessToken(friend);


        byte[] bytes = "test PostController".getBytes();
        MockMultipartFile mockFile =
                new MockMultipartFile("image", "image.jpg", String.valueOf(MediaType.IMAGE_JPEG), bytes);

        mvc.perform(MockMvcRequestBuilders
                        .multipart(HttpMethod.PATCH, "/api/v1/posts/{id}/image", postId)
                        .file(mockFile)
                        .header("Authorization", TOKEN_PREFIX + " " + tokenFriend))
                .andDo(print())
                .andExpect(status().is(403));
    }

    @Test
    void updatePicturePostNegativeStatus400Test() throws Exception {
        User user = userService.createUser(getUser());
        String token = jwtTokenProvider.createAccessToken(user);

        long postId = -1L;
        byte[] bytes = "test PostController".getBytes();
        MockMultipartFile mockFile =
                new MockMultipartFile("image", "image.jpg", String.valueOf(MediaType.IMAGE_JPEG), bytes);

        doReturn(true).when(postService).isOwnerPost(any(Long.class), any(Long.class));

        mvc.perform(MockMvcRequestBuilders
                        .multipart(HttpMethod.PATCH, "/api/v1/posts/{id}/image", postId)
                        .file(mockFile)
                        .header("Authorization", TOKEN_PREFIX + " " + token))
                .andDo(print())
                .andExpect(status().is(400));
    }

    @Test
    void updatePostPositiveTest() throws Exception {
        User user = userService.createUser(getUser());
        String token = jwtTokenProvider.createAccessToken(user);
        long postId = 10L;
        CreatePost createPost = givenCreatePost();
        createPost.setId(1L);
        JSONObject jsonObject = givenCreatePostJson(createPost);

        Post post = givenPost(createPost, user);


        doReturn(true).when(postService).isOwnerPost(any(Long.class), any(Long.class));
        doReturn(post).when(postService).updatePost(any(Post.class));


        mvc.perform(MockMvcRequestBuilders
                        .patch("/api/v1/posts/{id}", postId)
                        .header("Authorization", TOKEN_PREFIX + " " + token)
                        .content(jsonObject.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.id").value(post.getId()))
                .andExpect(jsonPath("$.title").value(post.getTitle()))
                .andExpect(jsonPath("$.content").value(post.getContent()))
                .andExpect(status().is(200));
    }

    @ParameterizedTest
    @MethodSource("argsProviderFactoryCreateDto")
    void updatePostNegativeContentTest(String param) throws Exception {
        User user = userService.createUser(getUser());
        String token = jwtTokenProvider.createAccessToken(user);
        long postId = 10L;
        CreatePost createPost = new CreatePost();
        createPost.setId(1L);
        createPost.setTitle("title post");
        createPost.setContent(param);
        JSONObject jsonObject = givenCreatePostJson(createPost);

        Post post = givenPost(createPost, user);

        doReturn(true).when(postService).isOwnerPost(any(Long.class), any(Long.class));

        mvc.perform(MockMvcRequestBuilders
                        .patch("/api/v1/posts/{id}", postId)
                        .header("Authorization", TOKEN_PREFIX + " " + token)
                        .content(jsonObject.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(400));
    }

    @ParameterizedTest
    @MethodSource("argsProviderFactoryCreateDto")
    void updatePostNegativeTitleTest(String param) throws Exception {
        User user = userService.createUser(getUser());
        String token = jwtTokenProvider.createAccessToken(user);
        long postId = 10L;
        CreatePost createPost = new CreatePost();
        createPost.setId(1L);
        createPost.setTitle(param);
        createPost.setContent("Content post");
        JSONObject jsonObject = givenCreatePostJson(createPost);

        Post post = givenPost(createPost, user);

        doReturn(true).when(postService).isOwnerPost(any(Long.class), any(Long.class));

        mvc.perform(MockMvcRequestBuilders
                        .patch("/api/v1/posts/{id}", postId)
                        .header("Authorization", TOKEN_PREFIX + " " + token)
                        .content(jsonObject.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(400));
    }

    @Test
    void updatePostNegativeIdTest() throws Exception {
        User user = userService.createUser(getUser());
        String token = jwtTokenProvider.createAccessToken(user);
        long postId = 10L;
        CreatePost createPost = givenCreatePost();
        JSONObject jsonObject = givenCreatePostJson(createPost);

        doReturn(true).when(postService).isOwnerPost(any(Long.class), any(Long.class));

        mvc.perform(MockMvcRequestBuilders
                        .patch("/api/v1/posts/{id}", postId)
                        .header("Authorization", TOKEN_PREFIX + " " + token)
                        .content(jsonObject.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(400));
    }

    @Test
    void deletePostPositiveTest() throws Exception {
        User user = userService.createUser(getUser());
        String token = jwtTokenProvider.createAccessToken(user);
        long postId = 10L;

        doReturn(true).when(postService).isOwnerPost(any(Long.class), any(Long.class));
        doNothing().when(postService).deletePost(any(Long.class));

        mvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/posts/{id}", postId)
                        .header("Authorization", TOKEN_PREFIX + " " + token))
                .andDo(print())
                .andExpect(status().is(204));
    }

    @Test
    void getPostPositiveTest() throws Exception{
        User user = userService.createUser(getUser());
        String token = jwtTokenProvider.createAccessToken(user);
        Post post = givenPost(givenCreatePost(), user);
        Long postId = post.getId();

        doReturn(post).when(postService).getPostById(any(Long.class));

        mvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/posts/{id}", postId)
                        .header("Authorization", TOKEN_PREFIX + " " + token))
                .andDo(print())
                .andExpect(jsonPath("$.id").value(post.getId()))
                .andExpect(jsonPath("$.title").value(post.getTitle()))
                .andExpect(jsonPath("$.content").value(post.getContent()))
                .andExpect(jsonPath("$.createdAt").value(post.getCreatedAt().toEpochMilli()))
                .andExpect(status().is(200));
    }

    @Test
    void getPostsPositiveTest() throws Exception{
        User user = userService.createUser(getUser());
        String token = jwtTokenProvider.createAccessToken(user);

        List<Post> posts = List.of(givenPost(givenCreatePost(), user), givenPost(givenCreatePost(), user));
        List<PostDto> postDtos = postMapper.postsToPostsDto(posts);

        doReturn(posts).when(postService).getPosts(any(Pageable.class));

        mvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/posts")
                        .header("Authorization", TOKEN_PREFIX + " " + token))
                .andDo(print())
                .andExpect(content().json(objectMapper.writeValueAsString(postDtos)))
                .andExpect(status().is(200));

    }

    @Test
    void getPostsNegativeTest() throws Exception{
        User user = userService.createUser(getUser());
        String token = jwtTokenProvider.createAccessToken(user);

        mvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/posts")
                        .param("column", "titles")
                        .header("Authorization", TOKEN_PREFIX + " " + token))
                .andDo(print())
                .andExpect(status().is(400));
    }

    @Test
    void getPostsFriendPositiveTest() throws Exception{
        User user = userService.createUser(getUser());
        String token = jwtTokenProvider.createAccessToken(user);

        List<Post> posts = List.of(givenPost(givenCreatePost(), user), givenPost(givenCreatePost(), user));
        List<PostDto> postDtos = postMapper.postsToPostsDto(posts);

        doReturn(posts).when(postService).getPostsByFriend(any(SecurityUser.class), any(Pageable.class));

        mvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/posts/friends")
                        .header("Authorization", TOKEN_PREFIX + " " + token))
                .andDo(print())
                .andExpect(content().json(objectMapper.writeValueAsString(postDtos)))
                .andExpect(status().is(200));

    }

    @Test
    void getPostsFriendNegativeTest() throws Exception{
        User user = userService.createUser(getUser());
        String token = jwtTokenProvider.createAccessToken(user);

        mvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/posts/friends")
                        .param("column", "conte")
                        .header("Authorization", TOKEN_PREFIX + " " + token))
                .andDo(print())
                .andExpect(status().is(400));
    }


    private User getUser() {
        User user = new User();
        user.setName("user");
        user.setUsername("user@gmail.ru");
        user.setPassword("100");
        return user;
    }

    private User getFriend() {
        User user = new User();
        user.setName("friend");
        user.setUsername("friend@gmail.ru");
        user.setPassword("100");
        return user;
    }

    private SecurityUser givenSecurityUser(User user) {
        SecurityUser securityUser = new SecurityUser();
        securityUser.setId(user.getId());
        securityUser.setName(user.getName());
        securityUser.setUsername(user.getUsername());
        securityUser.setPassword(user.getPassword());
        securityUser.setAuthorities(
                user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName())).toList()
        );
        return securityUser;
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

    static Stream<String> argsProviderFactoryCreateDto() {
        return Stream.of("", "sw");
    }

    private JSONObject givenCreatePostJson(CreatePost createPost) throws JSONException {
        JSONObject object = new JSONObject();
        object.put("id", createPost.getId());
        object.put("title", createPost.getTitle());
        object.put("content", createPost.getContent());
        return object;
    }

}