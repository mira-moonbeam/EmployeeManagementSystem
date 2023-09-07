package com.employeemanagement.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.employeemanagement.model.TerminatedEmployee;

public interface TerminatedEmployeeRepository extends JpaRepository<TerminatedEmployee, Long> {
}
