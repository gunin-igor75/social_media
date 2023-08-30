package com.github.guninigor75.social_media.config;

import com.github.guninigor75.social_media.mapper.UserMapper;
import com.github.guninigor75.social_media.repository.UserRepository;
import com.github.guninigor75.social_media.security.JwtTokenProvider;
import com.github.guninigor75.social_media.security.SecurityUserDetailsService;
import com.github.guninigor75.social_media.service.props.JwtProperties;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.time.Duration;

@TestConfiguration
public class ControllerConfiguration {


    @Bean
    @Primary
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
    @Primary
    public JwtTokenProvider jwtTokenProviderTest() {
        return new JwtTokenProvider(jwtPropertiesTest());
    }

    @Bean
    @Primary
    public UserDetailsService userDetailsService() {
        return new  SecurityUserDetailsService(userRepository());
    }

    @Bean
    @Primary
    public UserRepository userRepository() {
        return Mockito.mock(UserRepository.class);
    }

    @Bean
    public UserMapper userMapperTest() {
        return Mappers.getMapper(UserMapper.class);
    }
}
