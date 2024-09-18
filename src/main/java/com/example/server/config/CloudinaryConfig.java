package com.example.server.config;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        final Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "dj70vxnto");
        config.put("api_key", "579269346374183");
        config.put("api_secret", "DrVnZDwZseCNf5Z66N6vg-zRAx8");

        return new Cloudinary(config);
    }
}
