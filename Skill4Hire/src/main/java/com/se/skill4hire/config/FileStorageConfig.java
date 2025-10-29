package com.se.skill4hire.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class FileStorageConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Serve files from uploads directory in current working dir and also parent (../uploads)
        String[] locations = new String[]{
                "file:uploads/",
                "file:../uploads/"
        };

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(locations);

        // Also add the API endpoint pattern used by the frontend
        registry.addResourceHandler("/api/uploads/**")
                .addResourceLocations(locations);
    }
}