package com.github.guninigor75.social_media.service.imp;

import com.github.guninigor75.social_media.entity.activity.Picture;
import com.github.guninigor75.social_media.exception_handler.ResourceNotFoundException;
import com.github.guninigor75.social_media.repository.PictureRepository;
import com.github.guninigor75.social_media.service.FileManagerService;
import com.github.guninigor75.social_media.service.PictureService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class PictureServiceImp implements PictureService {

    private final PictureRepository pictureRepository;

    private final FileManagerService fileManagerService;

    @Override
    @Transactional
    public void deletePicture(Picture picture) {
        fileManagerService.deleteFile(picture.getFilePath());
        pictureRepository.delete(picture);
    }

    @Override
    @Transactional
    public Picture createPicture(Picture picture, MultipartFile file) {
        fileManagerService.addFile(file);
        return savePicture(picture, file, fileManagerService.getFilePath());
    }

    @Override
    @Transactional
    public Picture updatePicture(Picture picture, MultipartFile file) {
        fileManagerService.replaceFile(file, picture.getFilePath());
        return savePicture(picture, file, fileManagerService.getFilePath());
    }

    @Override
    public Picture getPictureByIdPost(Long id) {
        return pictureRepository.findByPost_id(id).orElseThrow(() -> {
                    String message = "Picture with id post " + id + " is not in the database";
                    log.error(message);
                    return new ResourceNotFoundException(message);
                }
        );
    }

    private Picture savePicture(Picture picture, MultipartFile file, String filePath) {
        picture.setFilePath(filePath);
        picture.setFileSize(file.getSize());
        picture.setMediaType(file.getContentType());
        picture.getPost().setImage(filePath);
        return pictureRepository.save(picture);
    }
}
