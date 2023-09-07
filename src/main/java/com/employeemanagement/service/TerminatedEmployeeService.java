package com.employeemanagement.service;

import com.employeemanagement.model.TerminatedEmployee;
import com.employeemanagement.repository.TerminatedEmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TerminatedEmployeeService {

    @Autowired
    private TerminatedEmployeeRepository terminatedEmployeeRepository;

    public List<TerminatedEmployee> showTerminated() {
        return terminatedEmployeeRepository.findAll();
    }
}
