package com.github.guninigor75.social_media.util;

import com.github.guninigor75.social_media.config.SecurityConfig;
import com.github.guninigor75.social_media.config.SocialMediaConfiguration;
import com.github.guninigor75.social_media.controller.FriendController;
import com.github.guninigor75.social_media.entity.user.Role;
import com.github.guninigor75.social_media.entity.user.User;
import com.github.guninigor75.social_media.repository.UserRepository;
import com.github.guninigor75.social_media.security.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(FriendController.class)
@Import({SecurityConfig.class, SocialMediaConfiguration.class})
public class TokenAuthenticationServiceTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserRepository userRepository;

    static final String TOKEN_PREFIX = "Bearer";

//    @Test
//    public void shouldNotAllowAccessToUnauthenticatedUsers() throws Exception {
//        mvc.perform(MockMvcRequestBuilders.get("/test")).andExpect(status().isForbidden());
//    }

    @Test
    public void shouldGenerateAuthToken() throws Exception {
        User user = getUser();
        String token = jwtTokenProvider.createAccessToken(user);
        Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
//        String token = TokenAuthenticationService.createToken("john");

//        assertNotNull(token);
        mvc.perform(get("/test").header("Authorization", TOKEN_PREFIX + " " + token)).andExpect(status().isOk());
    }

    public static User getUser() {
        Role role = new Role();
        role.setId(1);
        role.setName("ROLE_USER");
        User user = new User();
        user.setId(1L);
        user.setUsername("user@gmail.ru");
        user.setRoles(List.of(role));
        return user;
    }
}
