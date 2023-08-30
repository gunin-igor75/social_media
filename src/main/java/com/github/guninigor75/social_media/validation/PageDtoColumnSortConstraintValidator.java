package com.github.guninigor75.social_media.validation;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.data.domain.Sort;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PageDtoColumnSortConstraintValidator implements ConstraintValidator<ValidationColumnSortPageDto, Sort.Direction> {

    private Set<Sort.Direction> columns;


    @Override
    public void initialize(ValidationColumnSortPageDto constraintAnnotation) {
        columns =Stream.of(constraintAnnotation.valueColumns()).collect(Collectors.toSet());
        columns.add(null);
    }

    @Override
    public boolean isValid(Sort.Direction value, ConstraintValidatorContext constraintValidatorContext) {
        return columns.contains(value);
    }
}
