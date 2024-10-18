package com.atsin.LearningTesting.TestingPracticeProject.dto;

import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
//@EqualsAndHashCode
public class EmployeeDto{

       private Long id;
       private String name;
       private String email;
       private Double salary;

       @Override
       public boolean equals(Object o) {
              if (this == o) return true;
              if (!(o instanceof EmployeeDto that)) return false;
           return Objects.equals(this.id, that.getId()) && Objects.equals(this.name, that.getName()) && Objects.equals(this.email, that.getEmail()) && Objects.equals(this.salary, that.getSalary());
       }

       @Override
       public int hashCode() {
              return Objects.hash(this.id, this.name, this.email, this.salary);
       }
}
