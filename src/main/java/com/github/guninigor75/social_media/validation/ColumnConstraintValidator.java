package com.github.guninigor75.social_media.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Set;

public class ColumnConstraintValidator implements ConstraintValidator<ValidationColumn, String> {

    private Set<String> columns;

    @Override
    public void initialize(ValidationColumn constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String string, ConstraintValidatorContext constraintValidatorContext) {
        return false;
    }
}
