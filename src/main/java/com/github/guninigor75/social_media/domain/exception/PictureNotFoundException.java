package com.github.guninigor75.social_media.domain.exception;

public class PictureNotFoundException extends RuntimeException{
    public PictureNotFoundException() {
    }

    public PictureNotFoundException(String message) {
        super(message);
    }
}
