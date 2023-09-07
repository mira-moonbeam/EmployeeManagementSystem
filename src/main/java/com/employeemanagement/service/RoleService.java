package com.employeemanagement.service;

import com.employeemanagement.model.Role;
import com.employeemanagement.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public List<Role> showRoles() {
        return roleRepository.findAll();
    }
}
