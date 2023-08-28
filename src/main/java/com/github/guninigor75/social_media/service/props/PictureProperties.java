package com.github.guninigor75.social_media.service.props;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Data
@Service
@ConfigurationProperties(prefix = "picture")
@NoArgsConstructor
public class PictureProperties {

    private String path;


    public PictureProperties(String path) {
        this.path = path;
    }
}
