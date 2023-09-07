package com.employeemanagement.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id", updatable = false)
    private Long employee_id;

    @Column(name = "name")
    private String name;

    @Column(name = "salary")
    private Long salary;

    // Referential Integrity still enforced in postgresql
    @Column(name = "grade")
    private Integer grade;

    @Column(name = "total_bonus")
    private Long totalBonus;

    // Constructor
    public Employee() {
        this.name = null;
        this.salary = null;
        this.grade = null;
    }
    
    public Employee(String name, Long salary, Integer grade) {
        this.name = name;
        this.salary = salary;
        this.grade = grade;
    }

    // Getters and Setters
    public Long getEmployeeId() {
        return employee_id;
    }

    public String getName() {
        return name;
    }

    public Long getSalary() {
        return salary;
    }

    public Integer getGrade() {
        return grade;
    }

    public Long getTotalBonus() {
        return totalBonus;
    }

    public void setEmployeeId(Long id) {
        this.employee_id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSalary(Long salary) {
        this.salary = salary;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public void setTotalBonus(Long totalBonus) {
        this.totalBonus = totalBonus;
    }
}