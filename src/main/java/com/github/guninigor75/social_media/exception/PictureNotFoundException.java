package com.github.guninigor75.social_media.exception;

public class PictureNotFoundException extends RuntimeException{
    public PictureNotFoundException() {
    }

    public PictureNotFoundException(String message) {
        super(message);
    }
}
