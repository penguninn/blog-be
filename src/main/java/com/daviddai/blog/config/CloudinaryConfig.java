package com.daviddai.blog.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Configuration
public class CloudinaryConfig {

    @Value("${cloudinary.name}")
    private String CLOUD_NAME;

    @Value("${cloudinary.api_key}")
    private String CLOUD_API_KEY;

    @Value("${cloudinary.api_secret}")
    private String CLOUD_SECRET;

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(
                ObjectUtils.asMap(
                        "cloud_name", CLOUD_NAME,
                        "api_key", CLOUD_API_KEY,
                        "api_secret", CLOUD_SECRET));
    }
}
