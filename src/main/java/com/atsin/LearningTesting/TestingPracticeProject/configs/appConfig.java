package com.atsin.LearningTesting.TestingPracticeProject.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.modelmapper.ModelMapper;

@Configuration
public class appConfig {
    @Bean
    public ModelMapper getModelMapper(){
        return new ModelMapper();
    }
}


