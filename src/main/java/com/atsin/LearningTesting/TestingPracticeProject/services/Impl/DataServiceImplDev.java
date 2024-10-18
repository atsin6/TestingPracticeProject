package com.atsin.LearningTesting.TestingPracticeProject.services.Impl;

import com.atsin.LearningTesting.TestingPracticeProject.services.DataService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile("dev")
@Service
public class DataServiceImplDev implements DataService {

    @Override
    public String getData() {
        return "Dev Data";
    }
}

