package com.github.guninigor75.social_media.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.guninigor75.social_media.config.IntegrationSuite;
import com.github.guninigor75.social_media.dto.activity.MessageDto;
import com.github.guninigor75.social_media.entity.activity.Message;
import com.github.guninigor75.social_media.entity.user.User;
import com.github.guninigor75.social_media.mapper.MessageMapper;
import com.github.guninigor75.social_media.repository.UserRepository;
import com.github.guninigor75.social_media.security.JwtTokenProvider;
import com.github.guninigor75.social_media.service.MessageService;
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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.Instant;
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
@ExtendWith(MockitoExtension.class)
class MessageControllerTest extends IntegrationSuite {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private UserService userService;

    @MockBean
    private MessageService messageService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MessageMapper messageMapper;

    static final String TOKEN_PREFIX = "Bearer";

    @BeforeEach
    public void init() {
        userRepository.deleteAll();;
    }

    @Test
    void creteMessagePositiveTest() throws Exception{
        User user = userService.createUser(getUser());
        Long userId = user.getId();
        String token = jwtTokenProvider.createAccessToken(user);

        JSONObject object = givenMessageJson();
        Message message = getMessage();
        message.setSender(user);

        doReturn(message).when(messageService).createMessage(any(Long.class), any(Long.class), any(Message.class));

        mvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/messages/{id}", userId)
                        .header("Authorization", TOKEN_PREFIX + " " + token)
                        .content(object.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.id").value(message.getId()))
                .andExpect(jsonPath("$.content").value(message.getContent()))
                .andExpect(jsonPath("$.createdAt").value(message.getCreatedAt().toEpochMilli()))
                .andExpect(status().is(201));
    }

    @ParameterizedTest
    @MethodSource("argsProviderFactoryMessageDto")
    void createMessageNegativeMessageDtoTest(String content) throws Exception{
        User user = userService.createUser(getUser());
        String token = jwtTokenProvider.createAccessToken(user);
        Long userId = user.getId();

        JSONObject object = new JSONObject();
        object.put("content", content);

        mvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/messages/{id}", userId)
                        .header("Authorization", TOKEN_PREFIX + " " + token)
                        .content(object.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(400));
    }

    @Test
    void createMessageNegativeIdTest() throws Exception{
        User user = userService.createUser(getUser());
        String token = jwtTokenProvider.createAccessToken(user);

        JSONObject object = givenMessageJson();

        long id = -1L;
        mvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/messages/{id}", id)
                        .header("Authorization", TOKEN_PREFIX + " " + token)
                        .content(object.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(400));
    }

    @Test
    void getMessagesPositiveTest() throws Exception{
        User user = userService.createUser(getUser());

        String token = jwtTokenProvider.createAccessToken(user);

        List<Message> messages = getMessages();

        List<MessageDto> messageDtos = messageMapper.messagesToMessagesDto(messages);

        doReturn(messages).when(messageService).getMessages(any(Long.class), any(Pageable.class));

        mvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/messages")
                        .header("Authorization", TOKEN_PREFIX + " " + token))
                .andDo(print())
                .andExpect(content().json(objectMapper.writeValueAsString(messageDtos)))
                .andExpect(status().is(200));
    }


    @Test
    void getMessagesUserPositiveTest() throws Exception{
        User user = userService.createUser(getUser());
        String token = jwtTokenProvider.createAccessToken(user);

        List<Message> messages = getMessages();

        List<MessageDto> messageDtos = messageMapper.messagesToMessagesDto(messages);

        doReturn(messages).when(messageService).getMessagesUser(any(Long.class),any(Long.class), any(Pageable.class));

        long id = 2L;

        mvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/messages/{id}", id)
                        .header("Authorization", TOKEN_PREFIX + " " + token))
                .andDo(print())
                .andExpect(content().json(objectMapper.writeValueAsString(messageDtos)))
                .andExpect(status().is(200));
    }

    @Test
    void getMessagesUserNegativeTest() throws Exception{
        User user = userService.createUser(getUser());
        String token = jwtTokenProvider.createAccessToken(user);

        long id = -2L;

        mvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/messages/{id}", id)
                        .header("Authorization", TOKEN_PREFIX + " " + token))
                .andDo(print())
                .andExpect(status().is(400));
    }

    static Stream<String> argsProviderFactoryMessageDto() {
        return Stream.of("qw", "");
    }

    private JSONObject givenMessageJson() throws JSONException {
        JSONObject object = new JSONObject();
        object.put("content", "content");
        return object;
    }
    private Message getMessage() {
        Message message = new Message();
        message.setContent("Content");
        message.setId(1L);
        message.setRecipient(2L);
        message.setCreatedAt(Instant.now());
        return message;
    }

    private List<Message> getMessages() {
        Message message = getMessage();
        Message messageSecond = new Message();
        messageSecond.setId(2L);
        messageSecond.setContent("content 1");
        messageSecond.setCreatedAt(Instant.now());
        return List.of(message, messageSecond);
    }

    private User getUser() {
        User user = new User();
        user.setName("user");
        user.setUsername("user@gmail.ru");
        user.setPassword("100");
        return user;
    }
}