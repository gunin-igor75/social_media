package com.github.guninigor75.social_media.controller;

import com.github.guninigor75.social_media.config.IntegrationSuite;
import com.github.guninigor75.social_media.dto.user.UpdateUser;
import com.github.guninigor75.social_media.entity.user.User;
import com.github.guninigor75.social_media.repository.UserRepository;
import com.github.guninigor75.social_media.security.JwtTokenProvider;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@ExtendWith(MockitoExtension.class)
class UserControllerTest extends IntegrationSuite {

    @Autowired
    private MockMvc mvc;
    @SpyBean
    private UserService userService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private UserRepository userRepository;

    static final String TOKEN_PREFIX = "Bearer";

    @BeforeEach
    public void init() {
        userRepository.deleteAll();;
    }

    @Test
    void updatePositiveNameTest() throws Exception{
        User user = userService.createUser(getUser());
        String token = jwtTokenProvider.createAccessToken(user);
        UpdateUser updateUser = givenUpdateUser("newUser", null);
        JSONObject object = givenUpdateUserJson(updateUser);
        user.setName(updateUser.getName());

        doReturn(user).when(userService).updateUser(any(Long.class), any(UpdateUser.class));

        mvc.perform(MockMvcRequestBuilders
                        .patch("/api/v1/users")
                        .header("Authorization", TOKEN_PREFIX + " " + token)
                        .content(object.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.username").value(user.getUsername()))
                .andExpect(jsonPath("$.name").value(updateUser.getName()))
                .andExpect(status().is(200));
    }

    @Test
    void updatePositivePasswordTest() throws Exception{
        User user = userService.createUser(getUser());
        String token = jwtTokenProvider.createAccessToken(user);
        UpdateUser updateUser = givenUpdateUser(null, null);
        JSONObject object = givenUpdateUserJson(updateUser);

        doReturn(user).when(userService).updateUser(any(Long.class), any(UpdateUser.class));

        mvc.perform(MockMvcRequestBuilders
                        .patch("/api/v1/users")
                        .header("Authorization", TOKEN_PREFIX + " " + token)
                        .content(object.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.username").value(user.getUsername()))
                .andExpect(jsonPath("$.name").value(user.getName()))
                .andExpect(status().is(200));
    }


    @ParameterizedTest
    @MethodSource("argsProviderFactoryUpdateUser")
    void updateNegativeTest(String parameter) throws Exception{
        User user = userService.createUser(getUser());
        String token = jwtTokenProvider.createAccessToken(user);
        UpdateUser updateUser = givenUpdateUser("newUser", parameter);
        JSONObject object = givenUpdateUserJson(updateUser);


        mvc.perform(MockMvcRequestBuilders
                        .patch("/api/v1/users")
                        .header("Authorization", TOKEN_PREFIX + " " + token)
                        .content(object.toString())
                        .contentType(MediaType.APPLICATION_JSON))
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

    private UpdateUser givenUpdateUser(String name, String password) {
        UpdateUser updateUser = new UpdateUser();
        updateUser.setName(name);
        updateUser.setPassword(password);
        return updateUser;
    }
    private JSONObject givenUpdateUserJson(UpdateUser updateUser) throws JSONException {
        JSONObject object = new JSONObject();
        object.put("name", updateUser.getName());
        object.put("password", updateUser.getPassword());
        return object;
    }

    static Stream<String> argsProviderFactoryUpdateUser() {
        return Stream.of("as", "     ", "asasasssasasssasasasasasasasasasasas");
    }
}