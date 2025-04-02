package com.daviddai.blog.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EmailValidator implements ConstraintValidator<ValidEmail, String> {

    private static final String PATTERN = 
            "^[A-Za-z0-9._%+-]+" + // EMAIL
            "@[A-Za-z0-9.-]+" + // DOMAIN
            "\\.[A-Za-z]{2,}$"; // IP_DOMAIN

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        Pattern pattern = Pattern.compile(PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}
