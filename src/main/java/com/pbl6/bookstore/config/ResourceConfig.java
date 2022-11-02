package com.pbl6.bookstore.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.TimeUnit;

/**
 * @author lkadai0801
 * @since 30/10/2022
 */

@Configuration
public class ResourceConfig {
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/**")
//                .addResourceLocations("file:src/main/resources/static/")
//                .setCacheControl(CacheControl.maxAge(2, TimeUnit.HOURS).cachePublic());
//    }
}
