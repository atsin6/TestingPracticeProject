package com.atsin.LearningTesting.TestingPracticeProject.controllers;


import com.atsin.LearningTesting.TestingPracticeProject.TestContainerConfiguration;
import com.atsin.LearningTesting.TestingPracticeProject.dto.EmployeeDto;
import com.atsin.LearningTesting.TestingPracticeProject.entities.EmployeeEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;

@AutoConfigureWebTestClient(timeout = "100000")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) //Run a real server
@Import(TestContainerConfiguration.class)
public class AbstractIntegrationTest {

    //WebTestClient is only works with real server
    @Autowired
    WebTestClient webTestClient;

    EmployeeEntity testEmployee = EmployeeEntity.builder()
            .id(1L)
                .name("Atul")
                .email("Atul@atsin@gmail.com")
                .salary(80000D)
                .build();

    EmployeeDto testEmployeeDto = EmployeeDto.builder()
            .id(1L)
                .name("Atul")
                .email("Atul@atsin@gmail.com")
                .salary(80000D)
                .build();
}
