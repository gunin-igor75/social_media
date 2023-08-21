package com.github.guninigor75.social_media.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
//import ru.skypro.homework.exception_handling.FileCreateAndUpLoadException;
//import ru.skypro.homework.exception_handling.FileDeleteException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

/**
 * Утилитный класс для работы с файлами
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class FileManager {

    /**
     * Проверка файла (размер и contentType)
     * @param file - файл картинки
//     * @throws FileCreateAndUpLoadException - не соответствие нужным параметрам
     */
    public void checkFile(MultipartFile file) {
        if (isBadFile(file)) {
            String message = "file size zero or no picture";
            log.error(message);
//            throw new FileCreateAndUpLoadException(message);
        }
    }

    /**
     * Проверка и удаление файла
     * @param filePath - путь к файлу
//     * @throws FileDeleteException - исключение при удалении файла
     */
    public void checkExistFileAndDelete(String filePath) {
        if (filePath != null) {
            Path path = Paths.get(filePath);
            try {
                Files.deleteIfExists(path);
            } catch (IOException e) {
                String message = "File path" + filePath + "does not exist";
                log.error(message);
//                throw new FileDeleteException(message);
            }
        }
    }

    /**
     * Скачивание файла
     * @param file - файл картинки
     * @param filePath - путь куда скачивается файл
//     * @throws FileCreateAndUpLoadException - нецдачное сохранение файла
     */
    public void upLoadFile(MultipartFile file, Path filePath) {
        try {
            Files.createDirectories(filePath.getParent());
            Files.deleteIfExists(filePath);
            Files.write(filePath, file.getBytes());
        } catch (IOException e) {
            String message = "Error create or upload file";
            log.error(message);
//            throw new FileCreateAndUpLoadException(message);
        }
    }

    /**
     * Создание случайного имени файла
     * @param file - файл картинки
     * @param directory - директория где файл будет находиться
     * @return - путь, где находится сохраненный файл
     */
    public Path getRandomPath(MultipartFile file, String directory) {
        UUID uuid = UUID.randomUUID();
        String subsequence = uuid.toString();
        String filename = file.getOriginalFilename();
        String extension = getExtension(filename);
        return Path.of(directory, subsequence + "." + extension);
    }

    /**
     * Проверка файла на требуемы параметры:
     * не нулевой размер
     * не {@code null}
     * contentType - image
     * @param file - файл картинки
     * @return - {@code true} - плохой файл,  {@code false} - хорошый файл
     */
    private boolean isBadFile(MultipartFile file) {
        String filename = file.getOriginalFilename();
        return file.getSize() == 0
                || filename == null
                || !Objects.requireNonNull(file.getContentType()).contains("image");
    }

    /**
     * Извлечение расширеня файла
     * @param fileName - имя файла с расширением
     * @return - расширение файла
     */
    private String getExtension(String fileName) {
        if (fileName != null) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        }
        String message = "File is null";
        log.error(message);
//        throw new FileCreateAndUpLoadException(message);
        return null;
    }
}
