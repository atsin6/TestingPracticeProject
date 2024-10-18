package com.atsin.LearningTesting.TestingPracticeProject.controllers;

import com.atsin.LearningTesting.TestingPracticeProject.dto.EmployeeDto;
import com.atsin.LearningTesting.TestingPracticeProject.entities.EmployeeEntity;
import com.atsin.LearningTesting.TestingPracticeProject.repositories.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;




class EmployeeControllerTestIT extends AbstractIntegrationTest {

    @Autowired
    private EmployeeRepository employeeRepository;
//
    @BeforeEach
    void setUp() {

        employeeRepository.deleteAll();
    }

    @Test
    void testGetEmployeeById_success() {
        EmployeeEntity savedEmployee = employeeRepository.save(testEmployee);

        webTestClient.get()
                .uri("/employees/{id}", savedEmployee.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(savedEmployee.getId())
                .jsonPath("$.email").isEqualTo(testEmployeeDto.getEmail());
//                .isEqualTo(testEmployeeDto);//HashCode and Equal Method should be enabled
//                .value(employeeDto ->{
//                    assertThat(employeeDto.getEmail()).isEqualTo(savedEmployee.getEmail());
//                    assertThat(employeeDto.getSalary()).isEqualTo(savedEmployee.getSalary());
//                    assertThat(employeeDto.getId()).isEqualTo(savedEmployee.getId());
//                });
    }

    @Test
    void testGetEmployeeById_failure() {
        webTestClient.get()
                .uri("/employees/1")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testCreateNewEmployee_whenEmployeeAlreadyExits_thenThrowException() {
        EmployeeEntity savedEmployee = employeeRepository.save(testEmployee);

        webTestClient.post()
                .uri("/employees")
                .bodyValue(testEmployeeDto)
                .exchange()
                .expectStatus()
                .is5xxServerError();
    }

    @Test
    void testCreateNewEmployee_whenEmployeeDoesNotExist_thenCreateEmployee() {
        webTestClient.post()
                .uri("/employees")
                .bodyValue(testEmployeeDto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.email").isEqualTo(testEmployeeDto.getEmail())
                .jsonPath("$.name").isEqualTo(testEmployeeDto.getName());
    }

    @Test
    void testUpdateEmployee_whenEmployeeDoesNotExist_thenThrowException() {
        webTestClient.put()
                .uri("/employees/{id}", testEmployeeDto.getId())
                .bodyValue(testEmployeeDto)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testUpdateEmployee_whenAttemptingToUpdateEmail_thenThrowException() {
        EmployeeEntity savedEmployee = employeeRepository.save(testEmployee);
        testEmployeeDto.setEmail("Arjun@gmail.com");
        testEmployeeDto.setName("Arjun");

        webTestClient.put()
                .uri("/employees/{id}", savedEmployee.getId())
                .bodyValue(testEmployeeDto)
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    void testUpdateEmployee_whenEmployeeExists_thenUpdateEmployee() {
        EmployeeEntity savedEmployee = employeeRepository.save(testEmployee);
        testEmployeeDto.setId(savedEmployee.getId());
        testEmployeeDto.setName("Arjun");
        testEmployeeDto.setSalary(2342D);

        webTestClient.put()
                .uri("/employees/{id}", savedEmployee.getId())
                .bodyValue(testEmployeeDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(EmployeeDto.class)
                .isEqualTo(testEmployeeDto);
    }

    @Test
    void testDeleteEmployee_whenEmployeeDoesNotExist_thenThrowException() {
        webTestClient.delete()
                .uri("/employees/{id}", testEmployee.getId())
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testDeleteEmployee_whenEmployeeExists_thenDeleteEmployee() {
        EmployeeEntity savedEmployee = employeeRepository.save(testEmployee);

        webTestClient.delete()
                .uri("/employees/{id}", savedEmployee.getId())
                .exchange()
                .expectStatus().isNoContent()
                .expectBody(Void.class);

        webTestClient.delete()
                .uri("/employees/{id}", savedEmployee.getId())
                .exchange()
                .expectStatus().isNotFound();
    }
}









