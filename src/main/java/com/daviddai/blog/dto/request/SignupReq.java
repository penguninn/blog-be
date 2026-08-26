
package com.daviddai.blog.dto.request;

import java.io.Serializable;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SignupReq(
        @Pattern(regexp = "", message = "Email must be valid") String email,

        @Size(min = 5, max = 255, message = "Display name must be more than 5 characters and less than 255 characters") String displayName,

        String avatarUrl,

        @Pattern(regexp = "", message = "") String password) implements Serializable {
}
