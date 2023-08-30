package com.github.guninigor75.social_media.validation;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PageDtoColumnNameConstraintValidator implements ConstraintValidator<ValidationColumnNamePageDto, String> {

    private Set<String> columns;


    @Override
    public void initialize(ValidationColumnNamePageDto constraintAnnotation) {
        columns = Stream.of(constraintAnnotation.valueColumns()).collect(Collectors.toSet());
        columns.add(null);
    }


    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        return columns.contains(value);
    }
}
