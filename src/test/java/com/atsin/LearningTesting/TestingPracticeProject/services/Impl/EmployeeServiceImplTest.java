package com.atsin.LearningTesting.TestingPracticeProject.services.Impl;

import com.atsin.LearningTesting.TestingPracticeProject.TestContainerConfiguration;
import com.atsin.LearningTesting.TestingPracticeProject.dto.EmployeeDto;
import com.atsin.LearningTesting.TestingPracticeProject.entities.EmployeeEntity;
import com.atsin.LearningTesting.TestingPracticeProject.exceptions.ResourceNotFoundException;
import com.atsin.LearningTesting.TestingPracticeProject.repositories.EmployeeRepository;
import com.atsin.LearningTesting.TestingPracticeProject.services.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@Import(TestContainerConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Slf4j
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Spy
    private ModelMapper modelMapper;

    @InjectMocks
    private EmployeeServiceImpl employeeService;
    private EmployeeEntity mockEmployee;
    private EmployeeDto mockEmployeeDto;

    @BeforeEach
    void setUp() {
        mockEmployee  = EmployeeEntity.builder()
                .id(1L)
                .name("Atul")
                .email("Atul@atsin@gmail.com")
                .salary(80000D)
                .build();

        mockEmployeeDto = modelMapper.map(mockEmployee, EmployeeDto.class);
    }

    @Test
    void testGetEmployeeById_WhenEmployeeIdIsPresent_ThenReturnEmployeeDto(){
        Long id = mockEmployee.getId();
        // Assign
        when(employeeRepository.findById(id)).thenReturn(Optional.of(mockEmployee));//Stubbing

        // Act
        EmployeeDto employeeDto = employeeService.getEmployeeById(id);

        // Assert
        assertThat(employeeDto).isNotNull();
        assertThat(employeeDto.getId()).isEqualTo(mockEmployee.getId());
        assertThat(employeeDto.getEmail()).isEqualTo(mockEmployee.getEmail());
        verify(employeeRepository, only()).findById(id);
    }

    @Test
    void testGetEmployeeById_WhenEmployeeIdIsNotPresent_ThenThrowException(){
        // Arrange
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act and Assert
        assertThatThrownBy(() -> employeeService.getEmployeeById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Employee with id 1 not found");

        verify(employeeRepository).findById(1L);
    }

    @Test
    void testCreateNewEmployee_WhenValidEmployee_ThenCreateNewEmployeeDto(){
        //Assign
        when(employeeRepository.findByEmail(anyString())).thenReturn(List.of());
        when(employeeRepository.save(any(EmployeeEntity.class))).thenReturn(mockEmployee);

        //Act
        EmployeeDto employeeDto = employeeService.createNewEmployee(mockEmployeeDto);

        //Assert
        assertThat(employeeDto).isNotNull();
        assertThat(employeeDto.getEmail()).isEqualTo(mockEmployeeDto.getEmail());
        ArgumentCaptor<EmployeeEntity> employeeArgumentCaptor = ArgumentCaptor.forClass(EmployeeEntity.class);
        verify(employeeRepository).save(employeeArgumentCaptor.capture());//checking that if employeeRepository.save() method run for any EmployeeEntity input

        EmployeeEntity capturedEmployee = employeeArgumentCaptor.getValue();
        assertThat(capturedEmployee.getEmail()).isEqualTo(mockEmployee.getEmail());
    }

    @Test
    void testCreateNewEmployee_WhenAttemptingToCreateEmployeeWithExistingEmail_ThenThrowException(){
        // Arrange
        when(employeeRepository.findByEmail(mockEmployeeDto.getEmail())).thenReturn(List.of(mockEmployee));

        // Act and Assert
        assertThatThrownBy(() -> employeeService.createNewEmployee(mockEmployeeDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Employee with email " + mockEmployeeDto.getEmail() + " already exists");

        verify(employeeRepository).findByEmail(mockEmployeeDto.getEmail());
        verify(employeeRepository, never()).save(any(EmployeeEntity.class));
    }

    @Test
    void testUpdateEmployee_WhenValidEmployee_ThenUpdateEmployeeDto(){
        // Arrange
        when(employeeRepository.findById(mockEmployeeDto.getId())).thenReturn(Optional.of(mockEmployee));
        mockEmployeeDto.setName("Rishi");
        mockEmployeeDto.setSalary(123456D);

        EmployeeEntity newEmployee = modelMapper.map(mockEmployeeDto, EmployeeEntity.class);
        when(employeeRepository.save(any(EmployeeEntity.class))).thenReturn(newEmployee);

        //Act
        EmployeeDto employeeDto = employeeService.updateEmployee(mockEmployeeDto.getId(), mockEmployeeDto);

        // Assert
        assertThat(employeeDto).isNotNull();
        assertThat(employeeDto).isEqualTo(mockEmployeeDto);

        verify(employeeRepository).findById(mockEmployeeDto.getId());
        verify(employeeRepository).save(any(EmployeeEntity.class));
    }

    @Test
    void testUpdateEmployee_WhenEmployeeIsNotPresent_ThenThrowException(){
        //Arrange
        when(employeeRepository.findById(2L)).thenReturn(Optional.empty());

        //Act and Assert
        assertThatThrownBy(() -> employeeService.updateEmployee(2L, mockEmployeeDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Employee not found with id: " + 2L);

        verify(employeeRepository).findById(2L);
        verify(employeeRepository, never()).save(any(EmployeeEntity.class));
    }

    @Test
    void testUpdateEmployee_WhenAttemptingToUpdateEmail_ThenThrowException(){
        //Arrange
        when(employeeRepository.findById(mockEmployeeDto.getId())).thenReturn(Optional.of(mockEmployee));
        mockEmployeeDto.setName("Hariom");
        mockEmployeeDto.setEmail("Hariom@gmail.com");

        //Act and Assert
        assertThatThrownBy(() -> employeeService.updateEmployee(mockEmployeeDto.getId(), mockEmployeeDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("The email of the employee cannot be updated");

        verify(employeeRepository).findById(mockEmployeeDto.getId());
        verify(employeeRepository, never()).save(any(EmployeeEntity.class));
    }

    @Test
    void testDeleteEmployee_WhenValidEmployee_ThenDeleteEmployee(){
        //Arrange
        when(employeeRepository.existsById(mockEmployeeDto.getId())).thenReturn(true);

        //
        assertThatCode(() -> employeeService.deleteEmployee(mockEmployeeDto.getId()))
                .doesNotThrowAnyException();

        verify(employeeRepository).deleteById(mockEmployeeDto.getId());

    }

    @Test
    void testDeleteEmployee_WhenEmployeeDoesNotExist_ThenThrowException(){
        //Arrange
        when(employeeRepository.existsById(1L)).thenReturn(false);

        //act and assert
        assertThatThrownBy(() -> employeeService.deleteEmployee(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Employee was not found with id: 1");

        verify(employeeRepository, never()).deleteById(any());
        verify(employeeRepository).existsById(any());
    }


}










