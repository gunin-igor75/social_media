package com.github.guninigor75.social_media.service;

import com.github.guninigor75.social_media.entity.activity.Picture;
import jakarta.transaction.Transactional;
import org.springframework.web.multipart.MultipartFile;

public interface PictureService {

    Picture getPicture(Integer id);

    void deletePicture(Picture picture);

    Picture createPicture(Picture picture, MultipartFile file);

    Picture getPictureByIdPost(Long id);

    @Transactional
    void  updatePicture(Picture picture, MultipartFile file);
}
