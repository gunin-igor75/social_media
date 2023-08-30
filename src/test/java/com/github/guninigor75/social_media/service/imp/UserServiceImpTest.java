package com.github.guninigor75.social_media.service.imp;

import com.github.guninigor75.social_media.config.ServiceConfiguration;
import com.github.guninigor75.social_media.config.IntegrationSuite;
import com.github.guninigor75.social_media.dto.user.UpdateUser;
import com.github.guninigor75.social_media.entity.user.Invite;
import com.github.guninigor75.social_media.entity.user.Role;
import com.github.guninigor75.social_media.entity.user.User;
import com.github.guninigor75.social_media.exception_handler.ResourceNotFoundException;
import com.github.guninigor75.social_media.repository.InviteRepository;
import com.github.guninigor75.social_media.repository.UserRepository;
import com.github.guninigor75.social_media.security.SecurityUser;
import com.github.guninigor75.social_media.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@DataJpaTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(ServiceConfiguration.class)
class UserServiceImpTest extends IntegrationSuite {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InviteRepository inviteRepository;

    @BeforeEach
    void beforeEach() {
        userRepository.deleteAll();
        inviteRepository.deleteAll();
    }


    @Test
    void createUserPositive() {
        User user = givenUser();
        User actualeUser = userService.createUser(user);
        Collection<Role> roles = actualeUser.getRoles();
        assertThat(actualeUser.getId()).isNotNull();
        assertThat(roles.size()).isEqualTo(1);
    }

    @Test
    void createUserNegative() {
        User user = givenUser();
        userRepository.save(user);
        assertThatThrownBy(() -> userService.createUser(user))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void getUserByIdPositive() {
        User user = givenUser();
        User saveUser = userRepository.save(user);
        User actualeUser = userService.getUserById(saveUser.getId());
        assertThat(saveUser).isEqualTo(actualeUser);
    }

    @Test
    void updateUserPositiveTest() {
        User user = userService.createUser(givenUser());
        UpdateUser updateUser = givenUpdateUser();
        Long userId = user.getId();
        User newuser = userService.updateUser(userId, updateUser);

        assertThat(newuser.getId()).isEqualTo(user.getId());
        assertThat(newuser.getName()).isEqualTo(updateUser.getName());
        assertThat(newuser.getPassword()).isNotEqualTo(user.getPassword());
    }

    @Test
    void getUserByIdNegative() {
        User user = givenUser();
        userRepository.save(user);
        assertThatThrownBy(() -> userService.getUserById(1000L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getByUsernamePositive() {
        User user = givenUser();
        User saveUser = userRepository.save(user);
        User actualeUser = userService.getByUsername(user.getUsername());
        assertThat(actualeUser).isEqualTo(saveUser);
    }

    @Test
    void getByUsernameNegative() {
        User user = givenUser();
        userRepository.save(user);
        assertThatThrownBy(() -> userService.getByUsername("admin@gmail.ru"))
                .isInstanceOf(ResourceNotFoundException.class);
    }


    @Test
    void createRequestFriendshipPositive() {
        User user = userService.createUser(givenUser());
        User fiend = userService.createUser(givenFriend());

        userService.createRequestFriendship(user.getId(), fiend.getId());
        User userFriend = userRepository.findById(user.getId()).orElseThrow();
        Set<User> friends = userFriend.getFriends();
        assertThat(friends.size()).isEqualTo(1);
    }

    @Test
    void createFriendshipPositive() {
        User user = userService.createUser(givenUser());
        User friend = userService.createUser(givenFriend());

        userService.createRequestFriendship(user.getId(), friend.getId());
        userService.createRequestFriendship(friend.getId(),user.getId());

        Invite invite = inviteRepository.findInvate(user.getId(), friend.getId()).orElse(null);
        assert invite != null;
        assertThat(invite.getStatus()).isEqualTo(Invite.Status.ACCEPTED);

        friend = userRepository.findById(friend.getId()).orElseThrow();
        assertThat(friend.getFriends().contains(user)).isTrue();
    }

    @Test
    void createRequestFriendshipNegative() {
        User user = userService.createUser(givenUser());
        User fiend = userService.createUser(givenFriend());

        userService.createRequestFriendship(user.getId(), fiend.getId());
        userService.acceptedFriendShip(fiend.getId(), user.getId());

        assertThatThrownBy(() ->
                userService.createRequestFriendship(user.getId(), fiend.getId()))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void deleteFriendPositive() {
        User user = userService.createUser(givenUser());
        User friend = userService.createUser(givenFriend());

        userService.createRequestFriendship(user.getId(), friend.getId());
        userService.acceptedFriendShip(friend.getId(), user.getId());

        Invite invite = inviteRepository.findInvate(user.getId(), friend.getId()).orElseThrow();

        assertThat(invite.getStatus()).isEqualTo(Invite.Status.ACCEPTED);
        friend = userService.getUserById(friend.getId());
        assertThat(friend.getFriends().contains(user)).isTrue();

        userService.deleteFriend(friend.getId(), user.getId());
        friend = userService.getUserById(friend.getId());
        assertThat(friend.getFriends().size()).isEqualTo(0);

        user = userService.getUserById(user.getId());
        assertThat(user.getFriends().contains(friend)).isTrue();

        invite = inviteRepository.findInvate(user.getId(), friend.getId()).orElse(null);
        assertThat(invite).isNull();
    }

    @Test
    void rejectedFriendShipPositive() {
        User user = userService.createUser(givenUser());
        User friend = userService.createUser(givenFriend());

        userService.createRequestFriendship(friend.getId(), user.getId());
        userService.rejectedFriendShip(user.getId(), friend.getId());

        Invite invite = inviteRepository.findInvate(friend.getId(), user.getId()).orElse(null);

        assertThat(invite).isNull();
    }

    @Test
    void rejectedFriendShipNegative() {
        User user = userService.createUser(givenUser());
        User friend = userService.createUser(givenFriend());

        assertThatThrownBy(() -> {
            userService.rejectedFriendShip(user.getId(), friend.getId() + 100);
        }).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void acceptedFriendShipPositive() {
        User user = userService.createUser(givenUser());
        User friend = userService.createUser(givenFriend());

        userService.createRequestFriendship(user.getId(), friend.getId());
        userService.acceptedFriendShip(user.getId(), friend.getId());

        Invite invite = inviteRepository.findInvate(user.getId(), friend.getId()).orElseThrow();

        assertThat(invite.getStatus()).isEqualTo(Invite.Status.ACCEPTED);

    }

    @Test
    void acceptedFriendShipNegative() {
        User user = userService.createUser(givenUser());
        User friend = userService.createUser(givenFriend());

        assertThatThrownBy( () ->
                userService.acceptedFriendShip(user.getId(), friend.getId()))
                .isInstanceOf(IllegalStateException.class);

    }

    @Test
    void checkingCandidateForFriendshipTest() {
        User user = userService.createUser(givenUser());
        User friend = userService.createUser(givenFriend());

        assertThatThrownBy(() ->
                userService.createRequestFriendship(user.getId(), user.getId()))
                .isInstanceOf(IllegalStateException.class);
    }

    private  User givenUser() {
        User user = new User();
        user.setName("user");
        user.setUsername("user@gmail.ru");
        user.setPassword("100");
        return user;
    }

    private  User givenFriend() {
        User user = new User();
        user.setName("friend");
        user.setUsername("friend@gmail.ru");
        user.setPassword("100");
        return user;
    }

    private UpdateUser givenUpdateUser() {
        UpdateUser updateUser = new UpdateUser();
        updateUser.setName("New name");
        updateUser.setPassword("New password");
        return updateUser;
    }

}



















