package com.github.guninigor75.social_media.service.imp;

import com.github.guninigor75.social_media.entity.activity.Picture;
import com.github.guninigor75.social_media.exception.ResourceNotFoundException;
import com.github.guninigor75.social_media.repository.PictureRepository;
import com.github.guninigor75.social_media.service.PictureService;
import com.github.guninigor75.social_media.service.props.PictureProperties;
import com.github.guninigor75.social_media.util.FileManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

@Slf4j
@Service
@RequiredArgsConstructor
public class PictureServiceImp implements PictureService {

    private final PictureRepository pictureRepository;

    private final FileManager fileManager;

    private final PictureProperties pictureProperties;

    /**
     * Получение картики из БД по id
     *
     * @param id - индентификатор по каторому картинка хранится в БД не может быть {@code null}
     * @return - найденная картинка из БД
     * @throws ResourceNotFoundException -отсутствие картинки в БД
     */
    @Override
    public Picture getPicture(Integer id) {
        return pictureRepository.findById(id).orElseThrow(() -> {
                    String message = "Picture with " + id + " is not in the database";
                    log.error(message);
                    return new ResourceNotFoundException(message);
                }
        );
    }

    /**
     * Получение фото по типу фото и идентификатору хозяина
     * @param typeOwner - тип фото {@code Avatar} или {@code Picture}
     * @param id - индентификатор по каторому хозяин фото хранится в БД не может быть {@code null}
     * @return - найденное фото
     */
//    @Override
//    public Photo getPhotoByOwner(String typeOwner, Integer id) {
//        Optional<Photo> photoOrEmpty = photoRepository.findPhotoByOwner(typeOwner, id);
//        if (photoOrEmpty.isEmpty()) {
//            String message = "photo with " + id +  " does not exist in the database";
//            log.error(message);
//            throw new PhotoNotFoundException(message);
//        }
//        return photoOrEmpty.get();
//    }

    /**
     * Удаление картинки из БД
     *
     * @param picture - картинка пользователя или объявления
     */
    @Override
    @Transactional
    public void deletePicture(Picture picture) {
        String filePath = picture.getFilePath();
        fileManager.checkExistFileAndDelete(filePath);
        pictureRepository.delete(picture);
    }

    /**
     * Создание картинки
     *
     * @param picture - картинка
     * @param file    - файл картинки
     * @return - сохраненная картинка в БД
     */
    @Override
    @Transactional
    public Picture createPicture(Picture picture, MultipartFile file) {
        String filePath = downLoadFile(picture, file);
        return correctedPicture(picture, file, filePath);
    }

    /**
     * Изменение картинки
     *
     * @param picture - картинка
     * @param file    - файл картинки
     */
    @Override
    @Transactional
    public void updatePicture(Picture picture, MultipartFile file) {
        String filePath = downLoadFile(picture, file);
        correctedPicture(picture, file, filePath);
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

    /**
     * Скачивание фала
     *
     * @param picture - картинка
     * @param file    - файл картинки
     * @return - путь где сохранена картинка
     */
    private String downLoadFile(Picture picture, MultipartFile file) {
        Path filePath = fileManager.getRandomPath(file, pictureProperties.getPath());
        fileManager.checkExistFileAndDelete(picture.getFilePath());
        fileManager.upLoadFile(file, filePath);
        return filePath.toString();
    }

    /**
     * Изменение параметров картинки
     *
     * @param picture  - картинка
     * @param file     - файл картинки
     * @param filePath - путь где сохранена картинка
     * @return - сохраненная картинка в БД
     */
    private Picture correctedPicture(Picture picture, MultipartFile file, String filePath) {
        picture.setFilePath(filePath);
        picture.setFileSize(file.getSize());
        picture.setMediaType(file.getContentType());
        return pictureRepository.save(picture);
    }
}
