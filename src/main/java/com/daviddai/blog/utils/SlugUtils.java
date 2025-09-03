package com.daviddai.blog.utils;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class SlugUtils {
    
    private static final Pattern NON_ALPHANUMERIC = Pattern.compile("[^\\p{L}\\p{N}\\s]");
    private static final Pattern WHITESPACE = Pattern.compile("\\s+");
    private static final Pattern VIETNAMESE_D = Pattern.compile("[đĐ]");
    
    public static String generateSlug(String title) {
        if (title == null || title.trim().isEmpty()) {
            return "untitled-" + System.currentTimeMillis();
        }
        
        String slug = title.toLowerCase();
        
        slug = Normalizer.normalize(slug, Normalizer.Form.NFD);
        slug = slug.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        
        slug = VIETNAMESE_D.matcher(slug).replaceAll("d");
        
        slug = NON_ALPHANUMERIC.matcher(slug).replaceAll(" ");
        
        slug = WHITESPACE.matcher(slug.trim()).replaceAll("-");
        
        if (slug.isEmpty()) {
            slug = "post";
        }
        
        long timestamp = System.currentTimeMillis();
        return slug + "-" + timestamp;
    }
}