package com.github.guninigor75.social_media.controller;

import com.github.guninigor75.social_media.domain.activity.Picture;
import com.github.guninigor75.social_media.domain.exception.PictureNotFoundException;
import com.github.guninigor75.social_media.service.PictureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/image")
@RequiredArgsConstructor
@Slf4j
public class PictureController {

    private final PictureService pictureService;

    @GetMapping(value = "{id}",
            produces = {
                    MediaType.IMAGE_PNG_VALUE,
                    MediaType.IMAGE_JPEG_VALUE,
                    MediaType.IMAGE_GIF_VALUE,
                    MediaType.APPLICATION_OCTET_STREAM_VALUE
            })
    public ResponseEntity<byte[]> downLoadImage(@PathVariable("id") Long id) {
        Picture picture = pictureService.getPicture(id);
        Path path = Paths.get(picture.getFilePath());
        byte[] bytes = getBytes(path);
        return ResponseEntity.status(HttpStatus.OK).body(bytes);
    }

    private byte[] getBytes(Path path) {
        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            String message = "File path " + path + " does not exist";
            log.error(message);
            throw new PictureNotFoundException(message);
        }
    }
}
