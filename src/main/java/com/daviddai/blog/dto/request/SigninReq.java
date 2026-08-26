package com.daviddai.blog.dto.request;

import jakarta.validation.constraints.Pattern;

public record SigninReq(
        @Pattern(regexp = "", message = "Email must be valid") String email,
        @Pattern(regexp = "", message = "") String password) {
}
