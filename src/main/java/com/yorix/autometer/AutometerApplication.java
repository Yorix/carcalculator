package com.yorix.autometer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:constants.properties")
public class AutometerApplication {
    public static void main(String[] args) {
        SpringApplication.run(AutometerApplication.class, args);
    }
}
