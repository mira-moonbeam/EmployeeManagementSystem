package com.employeemanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.employeemanagement.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
