package com.daviddai.blog.utils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import com.daviddai.blog.model.dtos.request.UserRegisterRequest;

public class MatchesPasswordValidator implements ConstraintValidator<MatchesPassword, Object> {

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext constraintValidatorContext) {
        UserRegisterRequest user = (UserRegisterRequest) obj;
        return user.getPassword().equals(user.getConfirmPassword());
    }

}
