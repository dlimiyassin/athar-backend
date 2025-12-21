package com.student.career.zBase.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:4200")
                .allowedMethods("DELETE", "GET", "POST", "PATCH", "PUT")
                .allowedHeaders("Access-Control-Allow-Headers", "Access-Control-Allow-Origin", "Access-Control-Request-Method", "Access-Control-Request-Headers", "Origin", "Cache-Control", "Content-Type", "Authorization", "Content-Disposition", "enctype")
                .exposedHeaders("Access-Control-Allow-Headers", "Access-Control-Allow-Origin", "Access-Control-Request-Method", "Access-Control-Request-Headers", "Origin", "Cache-Control", "Content-Type", "Authorization", "Content-Disposition", "enctype")
                .allowCredentials(true).maxAge(3600);

    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer
                .favorParameter(false) // Retain this to not favor request parameters
                .ignoreAcceptHeader(false) // Retain this to respect the Accept header
                .mediaType(MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_JPEG)
                .mediaType(MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_PNG)
                .mediaType(MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON)
                .mediaType(MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_XML)
                .mediaType("myXml", MediaType.APPLICATION_XML)
                .defaultContentType(MediaType.APPLICATION_JSON);
    }

}
