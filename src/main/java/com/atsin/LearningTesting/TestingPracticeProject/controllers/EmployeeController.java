package com.atsin.LearningTesting.TestingPracticeProject.controllers;




import com.atsin.LearningTesting.TestingPracticeProject.dto.EmployeeDto;
import com.atsin.LearningTesting.TestingPracticeProject.services.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;


    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable Long id){
        EmployeeDto employeeDto = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(employeeDto);
    }

    @PostMapping
    public ResponseEntity<EmployeeDto> createNewEmployee(@RequestBody EmployeeDto employeeDto){
        EmployeeDto createdEmployeeDto = employeeService.createNewEmployee(employeeDto);
        return new ResponseEntity<>(createdEmployeeDto, HttpStatus.CREATED);
    }
    @PutMapping(path = "/{id}")
    public ResponseEntity<EmployeeDto> updateEmployee(@RequestBody EmployeeDto employeeDTO,
                                                          @PathVariable Long id) {
        EmployeeDto updatedEmployeeDto = employeeService.updateEmployee(id, employeeDTO);
        return ResponseEntity.ok(updatedEmployeeDto);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id){
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }

}
