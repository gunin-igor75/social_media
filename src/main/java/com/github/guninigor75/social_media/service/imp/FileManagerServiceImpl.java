package com.github.guninigor75.social_media.service.imp;

import com.github.guninigor75.social_media.exception_handler.ImageUploadException;
import com.github.guninigor75.social_media.service.FileManagerService;
import com.github.guninigor75.social_media.service.props.PictureProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileManagerServiceImpl implements FileManagerService {

    private final PictureProperties pictureProperties;

    private String filePath;


    @Override
    public String getFilePath() {
        return filePath;
    }

    @Override
    public void addFile(MultipartFile file) {
        checkFile(file);
        Path path = getRandomPath(file);
        upLoadFile(file, path);
        filePath = path.toString();
    }

    @Override
    public void replaceFile(MultipartFile file, String filePath) {
        deleteFile(filePath);
        addFile(file);
    }

    @Override
    public void deleteFile(String filePath) {
        if (filePath != null) {
            Path path = Paths.get(filePath);
            try {
                Files.deleteIfExists(path);
            } catch (IOException e) {
                String message = "File path" + filePath + "does not exist";
                log.error(message);
                throw new ImageUploadException(message);
            }
        }
    }

    public Path getRandomPath(MultipartFile file) {
        UUID uuid = UUID.randomUUID();
        String subsequence = uuid.toString();
        String filename = file.getOriginalFilename();
        String extension = getExtension(filename);
        return Path.of(pictureProperties.getPath(), subsequence + "." + extension);
    }

    public void checkFile(MultipartFile file) {
        if (!isGoodFile(file)) {
            String message = "file size zero or no picture";
            log.error(message);
            throw new ImageUploadException(message);
        }
    }

    private boolean isGoodFile(MultipartFile file) {
        String filename = file.getOriginalFilename();
        return file.getSize() != 0 && filename != null;
    }

    private String getExtension(String fileName) {
        if (fileName != null) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        }
        String message = "Filename is null";
        log.error(message);
        throw new ImageUploadException(message);
    }

    public void upLoadFile(MultipartFile file, Path filePath) {
        try {
            Files.createDirectories(filePath.getParent());
            Files.deleteIfExists(filePath);
            Files.write(filePath, file.getBytes());
        } catch (IOException e) {
            String message = "Error create or upload file";
            log.error(message);
            throw new ImageUploadException(message);
        }
    }
}
