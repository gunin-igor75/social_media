package com.github.guninigor75.social_media.service.imp;

import com.github.guninigor75.social_media.config.IntegrationSuite;
import com.github.guninigor75.social_media.config.ServiceConfiguration;
import com.github.guninigor75.social_media.dto.activity.CreatePost;
import com.github.guninigor75.social_media.dto.activity.PageDto;
import com.github.guninigor75.social_media.entity.activity.Post;
import com.github.guninigor75.social_media.entity.user.User;
import com.github.guninigor75.social_media.exception_handler.ResourceNotFoundException;
import com.github.guninigor75.social_media.repository.PictureRepository;
import com.github.guninigor75.social_media.repository.PostRepository;
import com.github.guninigor75.social_media.repository.UserRepository;
import com.github.guninigor75.social_media.security.SecurityUser;
import com.github.guninigor75.social_media.service.FileManagerService;
import com.github.guninigor75.social_media.service.PostService;
import com.github.guninigor75.social_media.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@DataJpaTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(ServiceConfiguration.class)
class PostServiceImpTest extends IntegrationSuite {
    @Autowired
    private PostService postService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PictureRepository pictureRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private FileManagerService fileManagerService;

    @BeforeEach
    public void init() {
        pictureRepository.deleteAll();
        postRepository.deleteAll();
        userRepository.deleteAll();;
    }


    @Test
    void createPostWithFile() {
        User user = userService.createUser(givenUser());
        Post post = givenPost();
        SecurityUser securityUser = givenSecurityUser(user);
        MultipartFile file = givenFile();

        Post actualPost = postService.createPost(post, file, securityUser);

        assertThat(actualPost.getId()).isNotNull();
        assertThat(actualPost.getImage()).isNotNull();
        assertThat(actualPost.getCreatedAt()).isNotNull();

        fileManagerService.deleteFile(post.getImage());
    }

    @Test
    void createPostWithOutFile() {
        User user = userService.createUser(givenUser());
        Post post = givenPost();
        SecurityUser securityUser = givenSecurityUser(user);
        MultipartFile file = null;

        Post actualPost = postService.createPost(post, file, securityUser);

        assertThat(actualPost.getId()).isNotNull();
        assertThat(actualPost.getImage()).isNull();
        assertThat(actualPost.getCreatedAt()).isNotNull();
    }


    @Test
    void deletePostTestPositive() {
        User user = userService.createUser(givenUser());
        Post post = givenPost();
        SecurityUser securityUser = givenSecurityUser(user);
        MultipartFile file = givenFile();

        Post actualPost = postService.createPost(post, file, securityUser);
        assertThat(postRepository.count()).isEqualTo(1);
        assertThat(pictureRepository.count()).isEqualTo(1);
        String image = actualPost.getImage();

        Long postId = actualPost.getId();
        postService.deletePost(postId);
        assertThat(postRepository.count()).isEqualTo(0);
        assertThat(pictureRepository.count()).isEqualTo(0);

        fileManagerService.deleteFile(image);

    }

    @Test
    void getPostByIdPositiveTest() {
        User user = userService.createUser(givenUser());
        Post post = givenPost();
        SecurityUser securityUser = givenSecurityUser(user);
        MultipartFile file = null;

        Post savePost = postService.createPost(post, file, securityUser);
        Long postId = savePost.getId();

        Post actualePost = postService.getPostById(postId);
        assertThat(savePost).isEqualTo(actualePost);

    }

    @Test
    void getPostByIdNegativeTest() {
        User user = userService.createUser(givenUser());
        Post post = givenPost();
        SecurityUser securityUser = givenSecurityUser(user);
        MultipartFile file = null;

        Post actualPost = postService.createPost(post, file, securityUser);
        Long postId = actualPost.getId();
        assertThatThrownBy(() -> {
            postService.getPostById(postId + 1);
        } ).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void updatePictureByPostIdWasPicturePositiveTest() {
        User user = userService.createUser(givenUser());
        Post post = givenPost();
        SecurityUser securityUser = givenSecurityUser(user);
        MultipartFile file = givenFile();

        Post oldPost = postService.createPost(post, file, securityUser);
        Long postId = oldPost.getId();
        String imageOldPost = oldPost.getImage();


        MultipartFile fileUpdate = givenFileUpdate();
        Post newPost = postService.updatePictureByPostId(postId, fileUpdate);

        assertThat(postRepository.count()).isEqualTo(1);
        assertThat(pictureRepository.count()).isEqualTo(1);
        assertThat(oldPost.getId()).isEqualTo(newPost.getId());
        assertThat(oldPost.getTitle()).isEqualTo(newPost.getTitle());
        assertThat(oldPost.getUpdatedAt()).isNotEqualTo(newPost.getUpdatedAt());
        assertThat(oldPost.getImage()).isNotEqualTo(newPost.getImage());
        assertThat(Files.notExists(Paths.get(imageOldPost))).isTrue();

        fileManagerService.deleteFile(newPost.getImage());
    }

    @Test
    void updatePictureByPostIdPositiveTest() {
        User user = userService.createUser(givenUser());
        Post post = givenPost();
        SecurityUser securityUser = givenSecurityUser(user);
        MultipartFile file = null;

        Post oldPost = postService.createPost(post, file, securityUser);
        Long postId = oldPost.getId();

        assertThat(oldPost.getImage()).isNull();


        file = givenFile();
        Post newPost = postService.updatePictureByPostId(postId, file);

        assertThat(postRepository.count()).isEqualTo(1);
        assertThat(pictureRepository.count()).isEqualTo(1);
        assertThat(oldPost.getId()).isEqualTo(newPost.getId());
        assertThat(oldPost.getTitle()).isEqualTo(newPost.getTitle());
        assertThat(newPost.getImage()).isNotNull();

        fileManagerService.deleteFile(newPost.getImage());
    }

    @Test
    void updatePostTest() {
        User user = userService.createUser(givenUser());
        Post post = givenPost();
        SecurityUser securityUser = givenSecurityUser(user);
        MultipartFile file = null;

        Post oldPost = postService.createPost(post, file, securityUser);
        Long postId = oldPost.getId();

        CreatePost createPost = givenCreatePost(postId);

        Post newPost = postService.updatePost(createPost);

        assertThat(oldPost.getId()).isEqualTo(newPost.getId());
        assertThat(newPost.getContent()).isEqualTo(createPost.getContent());
        assertThat(newPost.getTitle()).isEqualTo(createPost.getTitle());
    }

    @Test
    void getPostsTest() {
        User user = userService.createUser(givenUser());
        User friend = userService.createUser(givenFriend());

        Post post = givenPost();
        SecurityUser securityUser = givenSecurityUser(user);
        MultipartFile file = null;
        Post newPost = postService.createPost(post, file, securityUser);

        Post postFirst = givenPostFirst();
        securityUser = givenSecurityUser(friend);
        Post newPostFirst = postService.createPost(postFirst, file, securityUser);

        Post postSecond = givenPostSecond();
        Post newPostSecond = postService.createPost(postSecond, file, securityUser);

        Pageable pageable = new PageDto().getPageable(new PageDto());

        List<Post> posts = List.of(newPost, newPostFirst, newPostSecond);

        List<Post> actualPosts = postService.getPosts(pageable);

        assertThat(actualPosts.size()).isEqualTo(3);
        assertThat(actualPosts).containsExactlyInAnyOrderElementsOf(posts);
    }

    @Test
    void getPostsByFriendTest() {
        User user = userService.createUser(givenUser());
        User friend = userService.createUser(givenFriend());
        User person = userService.createUser(givenPerson());
        Long userId = user.getId();
        Long friendId = friend.getId();

        userService.createRequestFriendship(userId, friendId);
        userService.acceptedFriendShip(friendId, userId);

        Post post = givenPost();
        Post postFirst = givenPostFirst();
        Post postSecond = givenPostSecond();
        SecurityUser securityUserFriend = givenSecurityUser(friend);
        MultipartFile file = null;

        Post newPost = postService.createPost(post, file, securityUserFriend);
        Post newPostFirst = postService.createPost(postFirst, file, securityUserFriend);

        SecurityUser securityUserPerson = givenSecurityUser(person);
        postService.createPost(postSecond, file, securityUserPerson);

        Pageable pageable = new PageDto().getPageable(new PageDto());

        SecurityUser securityUser = givenSecurityUser(user);
        List<Post> posts = List.of(newPost, newPostFirst);
        List<Post> actualPosts = postService.getPostsByFriend(securityUser, pageable);

        assertThat(actualPosts.size()).isEqualTo(2);
        assertThat(actualPosts).containsExactlyInAnyOrderElementsOf(posts);


    }
    private User givenUser() {
        User user = new User();
        user.setName("user");
        user.setUsername("user@gmail.ru");
        user.setPassword("100");
        return user;
    }

    private static User givenFriend() {
        User user = new User();
        user.setName("friend");
        user.setUsername("friend@gmail.ru");
        user.setPassword("100");
        return user;
    }

    private static User givenPerson() {
        User user = new User();
        user.setName("person");
        user.setUsername("person@gmail.ru");
        user.setPassword("100");
        return user;
    }

    private Post givenPost() {
        Post post = new Post();
        post.setTitle("Post1");
        post.setContent("This a post1");
        return post;
    }

    private Post givenPostFirst() {
        Post post = new Post();
        post.setTitle("Post2");
        post.setContent("This a post2");
        return post;
    }

    private Post givenPostSecond() {
        Post post = new Post();
        post.setTitle("Post3");
        post.setContent("This a post3");
        return post;
    }


    private SecurityUser givenSecurityUser(User user) {
        SecurityUser securityUser = new SecurityUser();
        securityUser.setId(user.getId());
        securityUser.setUsername(user.getUsername());
        securityUser.setName(user.getName());
        securityUser.setPassword(user.getPassword());
        return securityUser;
    }

    private  MultipartFile givenFile() {
        return new MockMultipartFile(
                "image",
                "image.jpg",
                MediaType.TEXT_PLAIN_VALUE,
                "post POst1".getBytes()
        );
    }

    private  MultipartFile givenFileUpdate() {
        return new MockMultipartFile(
                "image",
                "image.jpg",
                MediaType.TEXT_PLAIN_VALUE,
                "post POst1 Update".getBytes()
        );
    }

    private CreatePost givenCreatePost(Long id) {
        CreatePost createPost = new CreatePost();
        createPost.setId(id);
        createPost.setContent("New content");
        createPost.setTitle("New title");
        return createPost;
    }
}