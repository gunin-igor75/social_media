package com.github.guninigor75.social_media.controller;

import com.github.guninigor75.social_media.config.ControllerConfiguration;
import com.github.guninigor75.social_media.config.IntegrationSuite;
import com.github.guninigor75.social_media.config.SecurityConfig;
import com.github.guninigor75.social_media.dto.auth.JwtRequest;
import com.github.guninigor75.social_media.dto.auth.JwtResponse;
import com.github.guninigor75.social_media.entity.user.User;
import com.github.guninigor75.social_media.mapper.UserMapper;
import com.github.guninigor75.social_media.service.AuthService;
import com.github.guninigor75.social_media.service.UserService;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@Import({SecurityConfig.class, ControllerConfiguration.class})
class AuthControllerTest extends IntegrationSuite {

    @Autowired
    private MockMvc mvc;
    @MockBean
    private AuthService authService;
    @MockBean
    private UserService userService;

    @Autowired
    private UserMapper userMapper;



    @Test
    void registerPositiveTest() throws Exception {
        JSONObject object = givenUserDtoJson();
        User user = givenUser();

        when(userService.createUser(any(User.class))).thenReturn(user);

        mvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/auth/register")
                        .content(object.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.name").value(user.getName()))
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.username").value(user.getUsername()))
                .andExpect(status().is(201));

    }

    @ParameterizedTest
    @MethodSource("argsProviderFactoryNameOrPassword")
    public void registerNameNegativeTest(String nane) throws Exception {
        JSONObject object = givenUserDtoJsonWithOutNameOrPassword();
        object.put("name", nane);
        User user = givenUser();

        when(userService.createUser(any(User.class))).thenReturn(user);

        mvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/auth/register")
                        .content(object.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(400));
    }

    @ParameterizedTest
    @MethodSource("argsProviderFactoryUsername")
    void registerUsernameNegativeTest(String username) throws Exception {
        JSONObject object = givenUserDtoJsonWithOutUsername();
        object.put("username", username);
        User user = givenUser();

        when(userService.createUser(any(User.class))).thenReturn(user);

        mvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/auth/register")
                        .content(object.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(400));
    }

    @ParameterizedTest
    @MethodSource("argsProviderFactoryNameOrPassword")
    void registerPasswordNegativeTest(String password) throws Exception {
        JSONObject object = givenUserDtoJsonWithOutUsername();
        object.put("password", password);
        User user = givenUser();

        when(userService.createUser(any(User.class))).thenReturn(user);

        mvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/auth/register")
                        .content(object.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(400));
    }

    @Test
    void loginPositiveTest() throws Exception {
        JSONObject object = givenJwtRequestJson();
        JwtResponse jwtResponse = givenJwtResponse();

        when(authService.login(any(JwtRequest.class))).thenReturn(jwtResponse);

        mvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/auth/login")
                        .content(object.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(200));
    }

    @ParameterizedTest
    @MethodSource("argsProviderFactoryNameOrPassword")
    void loginNegativeUPasswordTest(String password) throws Exception{
        JSONObject object = givenJwtRequestJsonWithPassword();
        object.put("password", password);
        JwtResponse jwtResponse = givenJwtResponse();

        when(authService.login(any(JwtRequest.class))).thenReturn(jwtResponse);

        mvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/auth/login")
                        .content(object.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(400));

    }

    @ParameterizedTest
    @MethodSource("argsProviderFactoryUsername")
    void loginNegativeUsernameTest(String username) throws Exception{
        JSONObject object = givenJwtRequestJsonWithUsername();
        object.put("username", username);
        JwtResponse jwtResponse = givenJwtResponse();

        when(authService.login(any(JwtRequest.class))).thenReturn(jwtResponse);

        mvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/auth/login")
                        .content(object.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(400));

    }

    @Test
    void refreshPositiveTest() throws Exception{
        String refreshToken = "Refresh token";
        JwtResponse jwtResponse = givenJwtResponse();

        when(authService.refresh(any(String.class))).thenReturn(jwtResponse);

        mvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/auth/refresh")
                        .content(refreshToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(200));

    }
    static Stream<String> argsProviderFactoryNameOrPassword() {
        return Stream.of("as", "FFDGGYHHGFghdgshgshdgysgyysgyc", "");
    }

    static Stream<String> argsProviderFactoryUsername() {
        return Stream.of("as", "FFDGGYHHGFghdgshgshdgysgyysgyc", "", "@mail.ru", "yandex.ru@");
    }

    private JSONObject givenUserDtoJson() throws JSONException {
        JSONObject object = new JSONObject();
        object.put("name", "user");
        object.put("username", "user@gmail.ru");
        object.put("password", "100");
        return object;
    }

    private JSONObject givenJwtRequestJson() throws JSONException {
        JSONObject object = new JSONObject();
        object.put("username", "user@gmail.ru");
        object.put("password", "100");
        return object;
    }

    private JSONObject givenJwtRequestJsonWithPassword() throws JSONException {
        JSONObject object = new JSONObject();
        object.put("username", "user@gmail.ru");
        return object;
    }

    private JSONObject givenJwtRequestJsonWithUsername() throws JSONException {
        JSONObject object = new JSONObject();
        object.put("password", "100");
        return object;
    }

    private JwtResponse givenJwtResponse() {
        JwtResponse jwtResponse = new JwtResponse();
        jwtResponse.setId(1L);
        jwtResponse.setAccessToken("Access token");
        jwtResponse.setRefreshToken("Refresh token");
        jwtResponse.setUsername("user.gmail.ru");
        return jwtResponse;
    }

    private JSONObject givenUserDtoJsonWithOutNameOrPassword() throws JSONException {
        JSONObject object = new JSONObject();
        object.put("name", "user");
        object.put("username", "user@gmail.ru");
        return object;
    }

    private JSONObject givenUserDtoJsonWithOutUsername() throws JSONException {
        JSONObject object = new JSONObject();
        object.put("name", "user");
        object.put("password", "100");
        return object;
    }

    private User givenUser() {
        User user = new User();
        user.setId(1L);
        user.setName("user");
        user.setUsername("user@gmail.ru");
        user.setPassword("$2y$10$jYSUvtcyuROUIBCUDH4sdO4AwgRhy/pxjvrI8mdfRLjn9OjQIDyjG");
        return user;
    }
}