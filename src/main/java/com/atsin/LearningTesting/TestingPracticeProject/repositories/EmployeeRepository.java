package com.atsin.LearningTesting.TestingPracticeProject.repositories;


import com.atsin.LearningTesting.TestingPracticeProject.entities.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {

    List<EmployeeEntity> findByEmail(String email);
}
