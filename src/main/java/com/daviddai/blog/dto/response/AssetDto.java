package com.daviddai.blog.dto.response;

import lombok.Data;

@Data
public class AssetDto {
    private String publicId;
    private String url;
    private Integer width;
    private Integer height;
    private String format;
    private Long bytes;
}