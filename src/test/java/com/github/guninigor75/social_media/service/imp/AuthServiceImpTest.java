package com.github.guninigor75.social_media.service.imp;

import com.github.guninigor75.social_media.config.IntegrationSuite;
import com.github.guninigor75.social_media.config.ServiceConfiguration;
import com.github.guninigor75.social_media.dto.auth.JwtRequest;
import com.github.guninigor75.social_media.dto.auth.JwtResponse;
import com.github.guninigor75.social_media.entity.user.User;
import com.github.guninigor75.social_media.repository.UserRepository;
import com.github.guninigor75.social_media.service.AuthService;
import com.github.guninigor75.social_media.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
@DataJpaTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(ServiceConfiguration.class)
class AuthServiceImpTest extends IntegrationSuite {
    @Autowired
    private  UserService userService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthService authService;

    @BeforeEach
    public void init() {
        userRepository.deleteAll();
    }

    @Test
    void loginTest() {
        User user = userService.createUser(givenUser());
        JwtRequest jwtRequest = givenUserJwtRequest();

        JwtResponse jwtResponse = authService.login(jwtRequest);

        assertThat(jwtResponse.getId()).isEqualTo(user.getId());
        assertThat(jwtResponse.getUsername()).isEqualTo(user.getUsername());
        assertThat(jwtResponse.getAccessToken()).isNotNull();
        assertThat(jwtResponse.getRefreshToken()).isNotNull();
    }

    @Test
    void refreshTest() throws InterruptedException {
        User user = userService.createUser(givenUser());
        JwtRequest jwtRequest = givenUserJwtRequest();

        JwtResponse jwtResponseLogin = authService.login(jwtRequest);
        String refreshToken = jwtResponseLogin.getRefreshToken();

        Thread.sleep(1000);
        JwtResponse jwtResponseRefresh = authService.refresh(refreshToken);

        assertThat(jwtResponseLogin.getId()).isEqualTo(jwtResponseRefresh.getId());
        assertThat(jwtResponseLogin.getUsername()).isEqualTo(jwtResponseRefresh.getUsername());
        assertThat(jwtResponseLogin.getAccessToken()).isNotEqualTo(jwtResponseRefresh.getAccessToken());
        assertThat(jwtResponseLogin.getRefreshToken()).isNotEqualTo(jwtResponseRefresh.getRefreshToken());
    }


    private static User givenUser() {
        User user = new User();
        user.setName("user");
        user.setUsername("user@gmail.ru");
        user.setPassword("100");
        return user;
    }

    private static JwtRequest givenUserJwtRequest() {
        JwtRequest jwtRequest = new JwtRequest();
        jwtRequest.setUsername("user@gmail.ru");
        jwtRequest.setPassword("100");
        return jwtRequest;
    }
}