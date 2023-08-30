package com.github.guninigor75.social_media.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.springframework.data.domain.Sort;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PageDtoColumnSortConstraintValidator.class)
public @interface ValidationColumnSortPageDto {

    Sort.Direction[] valueColumns();

    String message() default "Column name ${validatedValue} is not valid, may be: {valueColumns} ";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
