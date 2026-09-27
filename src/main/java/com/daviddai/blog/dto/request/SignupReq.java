package com.daviddai.blog.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignupReq(

    @NotBlank(message = "Email is required") @Email(message = "Email must be valid") @Size(max = 254, message = "Email is too long") 
    String email,

    @NotBlank(message = "Display name is required") @Size(min = 5, max = 255, message = "Display name must be between 5 and 255 characters") 
    String displayName,

    @NotBlank(message = "Password is required") @Size(min = 8, max = 72, message = "Password must be between 8 and 72 characters") 
    String password) {
}
