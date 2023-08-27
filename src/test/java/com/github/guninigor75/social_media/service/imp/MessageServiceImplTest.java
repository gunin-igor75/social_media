package com.github.guninigor75.social_media.service.imp;

import com.github.guninigor75.social_media.config.IntegrationSuite;
import com.github.guninigor75.social_media.config.ServiceConfiguration;
import com.github.guninigor75.social_media.dto.activity.PageDto;
import com.github.guninigor75.social_media.entity.activity.Message;
import com.github.guninigor75.social_media.entity.user.User;
import com.github.guninigor75.social_media.repository.MessageRepository;
import com.github.guninigor75.social_media.repository.UserRepository;
import com.github.guninigor75.social_media.service.MessageService;
import com.github.guninigor75.social_media.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(ServiceConfiguration.class)
class MessageServiceImplTest extends IntegrationSuite {

    @Autowired
    private  MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @BeforeEach
    public void init() {
        messageRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void createMessagePositive() {
        User user = userService.createUser(givenUser());
        User friend = userService.createUser(givenFriendFirst());
        Message message = new Message("Message");
        Long userId = user.getId();
        Long friendId = friend.getId();
        userService.createRequestFriendship(userId, friendId);
        userService.acceptedFriendShip(friendId, userId);
        Message actualMessage = messageService.createMessage(userId, friendId, message);

        assertThat(actualMessage.getId()).isNotNull();
        assertThat(actualMessage.getCreatedAt()).isNotNull();
        assertThat(actualMessage.getRecipient()).isEqualTo(friendId);

    }

    @Test
    void createMessageNegative() {
        User user = userService.createUser(givenUser());
        User friend = userService.createUser(givenFriendFirst());
        Message message = new Message("Message");
        Long userId = user.getId();
        Long friendId = friend.getId();

        assertThatThrownBy(() -> messageService.createMessage(userId, friendId, message))
                .isInstanceOf(IllegalStateException.class);

    }

    @Test
    void getMessagesTest() {
        User user = userService.createUser(givenUser());
        User friendFirst = userService.createUser(givenFriendFirst());
        User friendSecond = userService.createUser(givenFriendSecond());

        Long userId = user.getId();
        Long friendFirstId = friendFirst.getId();
        userService.createRequestFriendship(userId, friendFirstId);
        Long friendSecondId = friendSecond.getId();
        userService.createRequestFriendship(userId, friendSecondId);
        userService.createRequestFriendship(friendFirstId, friendSecondId);
        userService.acceptedFriendShip(friendFirstId, userId);
        userService.acceptedFriendShip(friendSecondId, userId);
        userService.acceptedFriendShip(friendSecondId, friendFirstId);

        Message messageFirst = messageService.createMessage(userId, friendFirstId, new Message("Message 1"));
        Message messageSecond = messageService.createMessage(userId, friendFirstId, new Message("Message 2"));
        Message messageThree = messageService.createMessage(friendFirstId, friendSecondId, new Message("Message 3"));
        Pageable pageable = new PageDto().getPageable(new PageDto());

        List<Message> messages = messageService.getMessages(userId, pageable);
        assertThat(messages.size()).isEqualTo(2);
        assertThat(messages.contains(messageFirst)).isTrue();
        assertThat(messages.contains(messageSecond)).isTrue();
        assertThat(messages.contains(messageThree)).isFalse();
    }


    @Test
    void getMessagesUserTest() {
        User user = userService.createUser(givenUser());
        User friendFirst = userService.createUser(givenFriendFirst());
        User friendSecond = userService.createUser(givenFriendSecond());

        Long userId = user.getId();
        Long friendFirstId = friendFirst.getId();
        userService.createRequestFriendship(userId, friendFirstId);
        Long friendSecondId = friendSecond.getId();
        userService.createRequestFriendship(userId, friendSecondId);
        userService.createRequestFriendship(friendFirstId, friendSecondId);
        userService.acceptedFriendShip(friendFirstId, userId);
        userService.acceptedFriendShip(friendSecondId, userId);
        userService.acceptedFriendShip(friendSecondId, friendFirstId);

        Message messageFirst = messageService.createMessage(userId, friendFirstId, new Message("Message 1"));
        Message messageSecond = messageService.createMessage(userId, friendFirstId, new Message("Message 2"));
        Message messageThree = messageService.createMessage(friendFirstId, friendSecondId, new Message("Message 3"));
        Message messageFour = messageService.createMessage(userId, friendSecondId, new Message("Message 3"));
        Pageable pageable = new PageDto().getPageable(new PageDto());

        List<Message> messages = messageService.getMessagesUser(userId, friendFirstId, pageable);
        assertThat(messages.size()).isEqualTo(2);
        assertThat(messages.contains(messageFirst)).isTrue();
        assertThat(messages.contains(messageSecond)).isTrue();
        assertThat(messages.contains(messageThree)).isFalse();
        assertThat(messages.contains(messageFour)).isFalse();
    }
    private static User givenUser() {
        User user = new User();
        user.setName("user");
        user.setUsername("user@gmail.ru");
        user.setPassword("100");
        return user;
    }

    private static User givenFriendFirst() {
        User user = new User();
        user.setName("friendFirst");
        user.setUsername("friendFirst@gmail.ru");
        user.setPassword("100");
        return user;
    }

    private static User givenFriendSecond() {
        User user = new User();
        user.setName("friendSecond");
        user.setUsername("friendSecond@gmail.ru");
        user.setPassword("100");
        return user;
    }
}