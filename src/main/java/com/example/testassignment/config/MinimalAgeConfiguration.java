package com.example.testassignment.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "application.security.jwt")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MinimalAgeConfiguration {
    @Value("${spring.application.minimal-age}")
    Integer minimalAge;
}
