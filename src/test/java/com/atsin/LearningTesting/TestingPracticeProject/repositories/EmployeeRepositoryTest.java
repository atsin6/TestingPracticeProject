package com.atsin.LearningTesting.TestingPracticeProject.repositories;

import com.atsin.LearningTesting.TestingPracticeProject.TestContainerConfiguration;
import com.atsin.LearningTesting.TestingPracticeProject.entities.EmployeeEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

//@ActiveProfiles("dev")
@Import(TestContainerConfiguration.class)
@DataJpaTest//Automatically configures embedded database
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;
    private EmployeeEntity employee;
//    private EmployeeDto employeeDto;

    @BeforeEach
    void setUp() {
        employee = EmployeeEntity.builder()
                .id(1L)
                .name("Atul")
                .email("Atul@atsin@gmail.com")
                .salary(80000D)
                .build();
    }

    @Test
    void testFindByEmail_whenEmailIsPresent_thenReturnEmployee() {
        // Pattern for creating test
        // Arrange/Given
        employeeRepository.save(employee);

        // Act/When
        List<EmployeeEntity> employeeList = employeeRepository.findByEmail(employee.getEmail());

        // Assert/Then
        assertThat(employeeList).isNotNull();
        assertThat(employeeList).isNotEmpty();
        assertThat(employeeList.get(0).getEmail()).isEqualTo(employee.getEmail());
    }

    @Test
    void testFindByEmail_whenEmailIsNotFound_thenReturnEmptyEmployeeList() {
        // Arrange/Given
        String email = "notPresent.123@gmail.com";

        // Act/When
        List<EmployeeEntity> employeeList = employeeRepository.findByEmail(employee.getEmail());

        //Asset
        assertThat(employeeList).isNotNull();
        assertThat(employeeList).isEmpty();

    }
}