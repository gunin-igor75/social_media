package com.github.guninigor75.social_media.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileManagerService {

    String getFilePath();

    void addFile(MultipartFile file);


    void replaceFile(MultipartFile file, String filePath);

    void deleteFile(String filePath);
}
