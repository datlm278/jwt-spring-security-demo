package com.example.jwtspringsecuritydemo.common.config;

import com.example.jwtspringsecuritydemo.common.constant.Constant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Value("${ssw.swagger.enable}")
    private boolean enableSwagger;
    @Value("${ssw.cors.origins}")
    private String corsOrigins;
    @Value("${ssw.methods.allowed}")
    private String allowedMethods;
    @Value("${ssw.header.allowed}")
    private String allowedHeaders;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping(Constant.PREFIX_API_URL + "/**")
                .allowCredentials(true)
                .allowedOrigins(corsOrigins.split(","))
                .allowedMethods(allowedMethods.split(","))
                .allowedHeaders(allowedHeaders.split(","))
                .maxAge(3600)
                .exposedHeaders("Content-Disposition");
    }
}
