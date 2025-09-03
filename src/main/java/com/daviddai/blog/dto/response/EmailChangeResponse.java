package com.daviddai.blog.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EmailChangeResponse {
    private boolean verifyEmailTriggered;
    private String pendingEmail;
}