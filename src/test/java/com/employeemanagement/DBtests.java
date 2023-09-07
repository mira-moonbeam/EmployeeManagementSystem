package com.employeemanagement;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.employeemanagement.model.Employee;
import com.employeemanagement.model.TerminatedEmployee;
import com.employeemanagement.repository.EmployeeRepository;
import com.employeemanagement.repository.TerminatedEmployeeRepository;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class DBtests {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private TerminatedEmployeeRepository terminatedEmployeeRepository;

    @Test
    public void testCreateAndDeleteOneEntity() {
        String expectedName = "Mira";
        Long expectedSalary = 7000000L;
        Integer expectedGrade = 3;

        Employee employee = new Employee(expectedName, expectedSalary, expectedGrade);

        Employee savedEmployee = employeeRepository.save(employee);
        Employee foundEmployee = employeeRepository.findById(savedEmployee.getEmployeeId()).orElse(null);

        Long testId = foundEmployee.getEmployeeId();
        System.out.println("Generated Id: " + testId);

        assertThat(foundEmployee).isNotNull();

        assertThat(foundEmployee.getName()).isEqualTo(expectedName);
        assertThat(foundEmployee.getSalary()).isEqualTo(expectedSalary);
        assertThat(foundEmployee.getGrade()).isEqualTo(expectedGrade);

        // Grade = 3: Staff means a 3% bonus
        assertThat(foundEmployee.getTotalBonus()).isEqualTo((long)(expectedSalary*1.03));
        
        // Test any additional properties that your PostgreSQL triggers may have populated
        assertThat(foundEmployee.getEmployeeId()).isNotNull();
        
        // Delete it, and it should then move to Terminated Employees table
        employeeRepository.deleteById(testId);

        TerminatedEmployee terminatedEmployee = terminatedEmployeeRepository.findById(testId).orElse(null);

        assertThat(terminatedEmployee).isNotNull();
        assertThat(foundEmployee.getName()).isEqualTo(expectedName);
        assertThat(foundEmployee.getSalary()).isEqualTo(expectedSalary);
        assertThat(foundEmployee.getGrade()).isEqualTo(expectedGrade);

        terminatedEmployeeRepository.deleteById(testId);
    }

    @Test
    public void testIdUniqueness() {
        Employee employee = new Employee("Mira", 7000000L, 3);

        Employee savedEmployee = employeeRepository.save(employee);
        Employee foundEmployee = employeeRepository.findById(savedEmployee.getEmployeeId()).orElse(null);
        Long firstId = foundEmployee.getEmployeeId();

        Employee secondEmployee = new Employee("Mira The Second", 7100000L, 2);

        savedEmployee = employeeRepository.save(secondEmployee);
        foundEmployee = employeeRepository.findById(savedEmployee.getEmployeeId()).orElse(null);
        Long secondId = foundEmployee.getEmployeeId();

        assertThat(firstId).isNotEqualTo(secondId);

        Employee thirdEmployee = new Employee("Mira The Third", 7200000L, 3);

        employeeRepository.deleteById(secondId);

        savedEmployee = employeeRepository.save(thirdEmployee);
        foundEmployee = employeeRepository.findById(savedEmployee.getEmployeeId()).orElse(null);
        Long thirdId = foundEmployee.getEmployeeId();

        assertThat(secondId).isNotEqualTo(thirdId);

        employeeRepository.deleteById(thirdId);
        terminatedEmployeeRepository.deleteById(secondId);
        terminatedEmployeeRepository.deleteById(thirdId);

        Employee fourthEmployee = new Employee("Mira The Second The Second", 7100000L, 2);

        savedEmployee = employeeRepository.save(fourthEmployee);
        foundEmployee = employeeRepository.findById(savedEmployee.getEmployeeId()).orElse(null);
        Long fourthId = foundEmployee.getEmployeeId();

        assertThat(secondId).isEqualTo(fourthId);

        employeeRepository.deleteById(firstId);
        terminatedEmployeeRepository.deleteById(firstId);

        employeeRepository.deleteById(fourthId);
        terminatedEmployeeRepository.deleteById(fourthId);
        
    }
}
