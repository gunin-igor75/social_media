package com.github.guninigor75.social_media.controller;

import com.github.guninigor75.social_media.config.IntegrationSuite;
import com.github.guninigor75.social_media.entity.user.User;
import com.github.guninigor75.social_media.repository.InviteRepository;
import com.github.guninigor75.social_media.repository.UserRepository;
import com.github.guninigor75.social_media.security.JwtTokenProvider;
import com.github.guninigor75.social_media.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@SpringBootTest
@ExtendWith(MockitoExtension.class)
class FriendControllerTest extends IntegrationSuite {

    @Autowired
    private MockMvc mvc;

    @SpyBean
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InviteRepository inviteRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    static final String TOKEN_PREFIX = "Bearer";

    @BeforeEach
    public void init() {
        inviteRepository.deleteAll();;
        userRepository.deleteAll();
    }

    @Test
    void requestFriendshipPositiveTest() throws Exception{
        User user = userService.createUser(getUser());
        User friend = userService.createUser(getFriend());
        Long friendId = friend.getId();

        String token = jwtTokenProvider.createAccessToken(user);
        Mockito.doNothing().when(userService).createRequestFriendship(any(Long.class), any(Long.class));

        mvc.perform(MockMvcRequestBuilders
                .post("/api/v1/friends/{id}", friendId)
                .header("Authorization", TOKEN_PREFIX + " " + token))
                .andDo(print())
                .andExpect(status().is(200));
    }

    @Test
    void requestFriendshipNegativeTest() throws Exception{
        User user = userService.createUser(getUser());
        Long id = -1L;

        String token = jwtTokenProvider.createAccessToken(user);
        Mockito.doNothing().when(userService).createRequestFriendship(any(Long.class), any(Long.class));

        mvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/friends/{id}", id)
                        .header("Authorization", TOKEN_PREFIX + " " + token))
                .andDo(print())
                .andExpect(status().is(400));
    }

    @Test
    void requestAcceptFriendshipPositiveTest() throws Exception{
        User user = userService.createUser(getUser());
        User friend = userService.createUser(getFriend());
        Long friendId = friend.getId();

        String token = jwtTokenProvider.createAccessToken(user);
        Mockito.doNothing().when(userService).acceptedFriendShip(any(Long.class), any(Long.class));

        mvc.perform(MockMvcRequestBuilders
                        .patch("/api/v1/friends/accepted/{id}", friendId)
                        .header("Authorization", TOKEN_PREFIX + " " + token))
                .andDo(print())
                .andExpect(status().is(200));
    }

    @Test
    void requestAcceptFriendshipNegativeTest() throws Exception{
        User user = userService.createUser(getUser());
        Long id = -1L;

        String token = jwtTokenProvider.createAccessToken(user);
        Mockito.doNothing().when(userService).acceptedFriendShip(any(Long.class), any(Long.class));

        mvc.perform(MockMvcRequestBuilders
                        .patch("/api/v1/friends/accepted/{id}", id)
                        .header("Authorization", TOKEN_PREFIX + " " + token))
                .andDo(print())
                .andExpect(status().is(400));
    }

    @Test
    void requestARejectFriendshipPositiveTest() throws Exception{
        User user = userService.createUser(getUser());
        User friend = userService.createUser(getFriend());
        Long friendId = friend.getId();

        String token = jwtTokenProvider.createAccessToken(user);
        Mockito.doNothing().when(userService).rejectedFriendShip(any(Long.class), any(Long.class));

        mvc.perform(MockMvcRequestBuilders
                        .patch("/api/v1/friends/rejected/{id}", friendId)
                        .header("Authorization", TOKEN_PREFIX + " " + token))
                .andDo(print())
                .andExpect(status().is(200));
    }

    @Test
    void requestARejectFriendshipNegativeTest() throws Exception{
        User user = userService.createUser(getUser());
        Long id = -1L;

        String token = jwtTokenProvider.createAccessToken(user);
        Mockito.doNothing().when(userService).rejectedFriendShip(any(Long.class), any(Long.class));

        mvc.perform(MockMvcRequestBuilders
                        .patch("/api/v1/friends/rejected/{id}", id)
                        .header("Authorization", TOKEN_PREFIX + " " + token))
                .andDo(print())
                .andExpect(status().is(400));
    }

    @Test
    void requestDeleteFriendPositiveTest() throws Exception{
        User user = userService.createUser(getUser());
        User friend = userService.createUser(getFriend());
        Long friendId = friend.getId();

        String token = jwtTokenProvider.createAccessToken(user);
        Mockito.doNothing().when(userService).deleteFriend(any(Long.class), any(Long.class));

        mvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/friends/{id}", friendId)
                        .header("Authorization", TOKEN_PREFIX + " " + token))
                .andDo(print())
                .andExpect(status().is(204));
    }

    @Test
    void requestDeleteFriendNegativeTest() throws Exception{
        User user = userService.createUser(getUser());
        Long id = -1L;

        String token = jwtTokenProvider.createAccessToken(user);
        Mockito.doNothing().when(userService).deleteFriend(any(Long.class), any(Long.class));

        mvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/friends/{id}", id)
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
}