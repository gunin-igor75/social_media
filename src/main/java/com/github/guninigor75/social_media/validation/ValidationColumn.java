package com.github.guninigor75.social_media.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ColumnConstraintValidator.class)
public @interface ValidationColumn {

    String[] columns();

    String message() default "Column name is not valid ${validatedValue}, may bay: {columns} ";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
