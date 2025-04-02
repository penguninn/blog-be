package com.daviddai.blog.utils;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = { PasswordValidator.class })
public @interface ValidPassword {

    String message() default "password must be at lest 8 character";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
