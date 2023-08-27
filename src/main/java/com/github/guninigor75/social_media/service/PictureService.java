package com.github.guninigor75.social_media.service;

import com.github.guninigor75.social_media.entity.activity.Picture;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface PictureService {

    void deletePicture(Picture picture);

    Picture createPicture(Picture picture, MultipartFile file);

    Optional<Picture> getPictureByIdPost(Long id);

    Picture  updatePicture(Picture picture, MultipartFile file);

//    Optional<Picture> getPictureOrEmptyByIdPost(Long id);
}
