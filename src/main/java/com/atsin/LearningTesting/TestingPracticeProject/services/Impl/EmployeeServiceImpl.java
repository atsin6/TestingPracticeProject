package com.atsin.LearningTesting.TestingPracticeProject.services.Impl;

import com.atsin.LearningTesting.TestingPracticeProject.dto.EmployeeDto;
import com.atsin.LearningTesting.TestingPracticeProject.entities.EmployeeEntity;
import com.atsin.LearningTesting.TestingPracticeProject.exceptions.ResourceNotFoundException;
import com.atsin.LearningTesting.TestingPracticeProject.repositories.EmployeeRepository;
import com.atsin.LearningTesting.TestingPracticeProject.services.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;

    @Override
    public EmployeeDto getEmployeeById(Long id) {
        log.info("Fetching employee by id: {}", id);
        EmployeeEntity employeeEntity = employeeRepository.findById(id).orElseThrow(
                () -> {
                    log.error("Employee not found with id: {}", id);
                    return new ResourceNotFoundException("Employee with id " + id + " not found");
                });
        log.info("Successfully fetched employee by id: {}", id);
        return modelMapper.map(employeeEntity, EmployeeDto.class);
    }

    @Override
    public EmployeeDto createNewEmployee(EmployeeDto employeeDTO) {
        log.info("Creating new employee with email: {}", employeeDTO.getEmail());
        List<EmployeeEntity> existingEmployees = employeeRepository.findByEmail(employeeDTO.getEmail());
        if (!existingEmployees.isEmpty()) {
            log.error("Employee with email {} already exists", employeeDTO.getEmail());
            throw new RuntimeException("Employee with email " + employeeDTO.getEmail() + " already exists");
        }
        EmployeeEntity newEmployee = modelMapper.map(employeeDTO, EmployeeEntity.class);
        EmployeeEntity savedEmployee = employeeRepository.save(newEmployee);
        return modelMapper.map(savedEmployee, EmployeeDto.class);
    }

    @Override
    public EmployeeDto updateEmployee(Long id, EmployeeDto employeeDto) {
        log.info("Updating employee with id: {}", id);
        EmployeeEntity employee = employeeRepository.findById(id).orElseThrow(
                () -> {
                    log.error("Employee not found with id: {}", id);
                    return new ResourceNotFoundException("Employee not found with id: " + id);
                });
        if (!employee.getEmail().equals(employeeDto.getEmail())) {
            log.error("Attempted to update email with employee with id: {}", id);
            throw new RuntimeException("The email of the employee cannot be updated");
        }
        modelMapper.map(employeeDto, employee);
        employee.setId(id);
        EmployeeEntity savedEmployee = employeeRepository.save(employee);
        log.info("Successfully updated employee with id: {}", id);
        return modelMapper.map(savedEmployee, EmployeeDto.class);
    }

    @Override
    public void deleteEmployee(Long id) {
        boolean exist = employeeRepository.existsById(id);
        if (!exist) {
            log.error("Employee not found with id: {}", id);
            throw new ResourceNotFoundException("Employee was not found with id: " + id);
        }
        employeeRepository.deleteById(id);
        log.info("Successfully deleted employee with id: {}", id);
    }
}


    //    public List<EmployeeDto> getAllEmployees() {
    //        List<EmployeeEntity> employeeEntities = employeeRepository.findAll();
    //        return employeeEntities
    //                .stream()
    //                .map(employeeEntity -> modelMapper.map(employeeEntity, EmployeeDto.class))
    //                .collect(Collectors.toList());
    //    public EmployeeDto updatePartialEmployeeById(Long employeeId, Map<String, Object> updates) {
    //        isExistByEmployeeId(employeeId);
    //        EmployeeEntity employeeEntity = employeeRepository.findById(employeeId).get();
    //        updates.forEach((field, value) -> {
    //            Field fieldToBeUpdated = ReflectionUtils.findRequiredField(EmployeeEntity.class, field);
    //            fieldToBeUpdated.setAccessible(true);
    //            ReflectionUtils.setField(fieldToBeUpdated, employeeEntity, value);
    //        });
    //
    //        return modelMapper.map(employeeRepository.save(employeeEntity), EmployeeDto.class);
    //    }

//}