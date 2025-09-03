package com.daviddai.blog.utils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SlugUtilsTest {

    @Test
    public void testGenerateSlug_withVietnameseTitle() {
        String title = "Xin Chào Đẹp";
        String result = SlugUtils.generateSlug(title);
        
        assertTrue(result.startsWith("xin-chao-dep-"));
        assertTrue(result.contains("-"));
        assertTrue(result.matches("^[a-z0-9\\-]+$"));
    }

    @Test
    public void testGenerateSlug_withDiacritics() {
        String title = "Café à la Crème";
        String result = SlugUtils.generateSlug(title);
        
        assertTrue(result.startsWith("cafe-a-la-creme-"));
        assertTrue(result.matches("^[a-z0-9\\-]+$"));
    }

    @Test
    public void testGenerateSlug_withSpecialCharacters() {
        String title = "Hello, World! @#$%^&*()";
        String result = SlugUtils.generateSlug(title);
        
        assertTrue(result.startsWith("hello-world-"));
        assertTrue(result.matches("^[a-z0-9\\-]+$"));
    }

    @Test
    public void testGenerateSlug_withEmptyTitle() {
        String title = "";
        String result = SlugUtils.generateSlug(title);
        
        assertTrue(result.startsWith("untitled-"));
        assertTrue(result.matches("^[a-z0-9\\-]+$"));
    }

    @Test
    public void testGenerateSlug_withNullTitle() {
        String result = SlugUtils.generateSlug(null);
        
        assertTrue(result.startsWith("untitled-"));
        assertTrue(result.matches("^[a-z0-9\\-]+$"));
    }

    @Test
    public void testGenerateSlug_alwaysHasTimestamp() {
        String title = "Test Title";
        String result1 = SlugUtils.generateSlug(title);
        String result2 = SlugUtils.generateSlug(title);
        
        assertNotEquals(result1, result2);
        assertTrue(result1.matches("^test-title-\\d+$"));
        assertTrue(result2.matches("^test-title-\\d+$"));
    }
}