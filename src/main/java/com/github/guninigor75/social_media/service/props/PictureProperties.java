package com.github.guninigor75.social_media.service.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "picture")
public class PictureProperties {

    private String path;


    public PictureProperties(String path) {
        this.path = path;
    }
}
