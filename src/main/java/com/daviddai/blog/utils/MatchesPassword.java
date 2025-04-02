package com.daviddai.blog.utils;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Target({ ElementType.FIELD, ElementType.TYPE })
@Constraint(validatedBy = { MatchesPasswordValidator.class })
@Retention(RetentionPolicy.RUNTIME)
public @interface MatchesPassword {

    String message() default "confirm password does not match";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
