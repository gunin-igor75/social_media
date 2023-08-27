package com.github.guninigor75.social_media.config;


import com.github.guninigor75.social_media.repository.*;
import com.github.guninigor75.social_media.security.JwtTokenProvider;
import com.github.guninigor75.social_media.service.*;
import com.github.guninigor75.social_media.service.imp.*;
import com.github.guninigor75.social_media.service.props.JwtProperties;
import com.github.guninigor75.social_media.service.props.PictureProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Duration;

@TestConfiguration
@RequiredArgsConstructor
public class ServiceConfiguration {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final InviteRepository inviteRepository;

    private final MessageRepository messageRepository;

    private final PictureRepository pictureRepository;

    private final PostRepository postRepository;

    @Bean
    public UserService userServiceTest() {
        return new UserServiceImp(
                userRepository,
                roleServiceTest(),
                inviteServiceTest(),
                passwordEncoderTest());
    }

    @Bean
    public RoleService roleServiceTest() {
        return new RoleServiceImp(roleRepository);
    }

    @Bean
    public InviteService inviteServiceTest() {
        return new InviteServiceImpl(inviteRepository);
    }

    @Bean
    public PasswordEncoder passwordEncoderTest() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public MessageService messageServiceTest() {
        return new MessageServiceImpl(messageRepository, userServiceTest(),inviteServiceTest());
    }

    @Bean
    public JwtProperties jwtPropertiesTest() {
        String secret = "My Secret";
        Duration access = Duration.ofHours(1);
        Duration refresh = Duration.ofDays(30);
        JwtProperties jwtProperties = new JwtProperties();
        jwtProperties.setSecret(secret);
        jwtProperties.setAccess(access);
        jwtProperties.setRefresh(refresh);
        return jwtProperties;
    }

    @Bean
    public JwtTokenProvider jwtTokenProviderTest() {
        return new JwtTokenProvider(jwtPropertiesTest());
    }

    @Bean
    public AuthService authServiceTest() {
        return new AuthServiceImp(userServiceTest(),jwtTokenProviderTest());
    }

    @Bean
    public FileManagerService fileManagerServiceTest() {
        return new FileManagerServiceImpl(
                new PictureProperties("picture")
        );
    }

    @Bean
    public PictureService pictureServiceTest() {
        return new PictureServiceImp(pictureRepository, fileManagerServiceTest());
    }

    @Bean
    public PostService postServiceTest() {
        return new PostServiceImp(postRepository,userServiceTest(),pictureServiceTest());
    }
}
