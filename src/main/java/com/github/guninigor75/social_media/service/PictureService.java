package com.github.guninigor75.social_media.service;

import com.github.guninigor75.social_media.entity.activity.Picture;
import org.springframework.web.multipart.MultipartFile;

public interface PictureService {

    void deletePicture(Picture picture);

    Picture createPicture(Picture picture, MultipartFile file);

    Picture getPictureByIdPost(Long id);

    Picture  updatePicture(Picture picture, MultipartFile file);
}
