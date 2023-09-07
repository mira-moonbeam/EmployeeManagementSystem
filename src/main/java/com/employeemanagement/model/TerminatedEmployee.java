package com.employeemanagement.model;
import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "Terminated_Employee")
public class TerminatedEmployee {

    @Id
    @Column(name = "employee_id")
    private Long employeeId;

    @Column(name = "name")
    private String name;

    @Column(name = "salary")
    private Long salary;

    @Column(name = "grade")
    private Integer grade;

    @Column(name = "total_bonus")
    private Long totalBonus;

    @Column(name = "termination_date")
    private Timestamp terminationDate;

    // Getters and Setters
    
    public Long getEmployeeId() {
        return this.employeeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSalary() {
        return salary;
    }

    public void setSalary(Long salary) {
        this.salary = salary;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public Long getTotalBonus() {
        return totalBonus;
    }

    public void setTotalBonus(Long totalBonus) {
        this.totalBonus = totalBonus;
    }

    public Timestamp getTerminationDate() {
        return terminationDate;
    }

    public void setTerminationDate(Timestamp terminationDate) {
        this.terminationDate = terminationDate;
    }

}
