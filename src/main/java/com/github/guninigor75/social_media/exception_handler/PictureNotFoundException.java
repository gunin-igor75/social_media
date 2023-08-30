package com.github.guninigor75.social_media.exception_handler;

public class PictureNotFoundException extends RuntimeException{
    public PictureNotFoundException() {
    }

    public PictureNotFoundException(String message) {
        super(message);
    }
}
