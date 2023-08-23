package com.github.guninigor75.social_media.exception_handler;

/**
 * Класс исключений  - отсутствие прав доступа к ресурсу
 */
public class AccessDeniedException extends RuntimeException{
    public AccessDeniedException() {
    }

    public AccessDeniedException(String message) {
        super(message);
    }
}
