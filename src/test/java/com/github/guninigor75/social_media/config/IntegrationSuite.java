package com.github.guninigor75.social_media.config;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.lifecycle.Startables;
import org.testcontainers.utility.DockerImageName;

import java.util.Map;


@ContextConfiguration(initializers = IntegrationSuite.Initializer.class)
public class IntegrationSuite {

    private static final DockerImageName POSTGRES_IMAGE = DockerImageName.parse("postgres:15.3-alpine");

    public static final PostgreSQLContainer<?> POSTGRES_CONTAINER = new PostgreSQLContainer<>(POSTGRES_IMAGE);

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            POSTGRES_CONTAINER.withInitScript("db.sql");
            Startables.deepStart(POSTGRES_CONTAINER).join();

            ConfigurableEnvironment environment = applicationContext.getEnvironment();
            environment.getPropertySources().addFirst(new MapPropertySource(
                    "testcontainers",
                    Map.of(
                            "spring.datasource.url", POSTGRES_CONTAINER.getJdbcUrl(),
                            "spring.datasource.username", POSTGRES_CONTAINER.getUsername(),
                            "spring.datasource.password", POSTGRES_CONTAINER.getPassword()
                    )
            ));
        }
    }
}
