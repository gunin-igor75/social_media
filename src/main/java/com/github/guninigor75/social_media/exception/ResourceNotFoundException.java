package com.github.guninigor75.social_media.exception;

/**
 * Класс исключения - отсутствие сущности в вбд по id
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
