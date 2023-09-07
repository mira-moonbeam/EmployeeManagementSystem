package com.employeemanagement.service;

import com.employeemanagement.model.Employee;
import com.employeemanagement.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.Collections;

@Service
public class EmployeeService {
    
    @Autowired
    private EmployeeRepository employeeRepository;
    
    public Employee addEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public List<Employee> showEmployees(String name, Long id, List<Long> ids, Integer grade) {
        if (id != null) {
            Optional<Employee> optionalEmployee = employeeRepository.findById(id);
            return optionalEmployee.map(Collections::singletonList).orElse(Collections.emptyList());
        }
        if (name != null) {
            return employeeRepository.findByName(name);
        }
        if (ids != null) {
            return employeeRepository.findAllById(ids);
        }
        if (grade != null) {
            return employeeRepository.findByGrade(grade);
        }
        return employeeRepository.findAll();
    }
    
    public Optional<Employee> updateEmployee(long id, Employee newDetails) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        if (optionalEmployee.isPresent()) {
            Employee updateEmployee = optionalEmployee.get();
            if (newDetails.getName() != null) updateEmployee.setName(newDetails.getName());
            if (newDetails.getSalary() != null) updateEmployee.setSalary(newDetails.getSalary());
            if (newDetails.getGrade() != null) updateEmployee.setGrade(newDetails.getGrade());
            employeeRepository.save(updateEmployee);
        }
        return optionalEmployee;
    }
    
    public void deleteEmployee(List<Long> ids) {
        for (Long id : ids) {
            employeeRepository.deleteById(id);
        }
    }
}
