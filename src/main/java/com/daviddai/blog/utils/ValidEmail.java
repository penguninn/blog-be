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
@Constraint(validatedBy = { EmailValidator.class })
public @interface ValidEmail {

    String message() default "invalid email format";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
