package com.employeemanagement.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Role {
    @Id
    @Column(name = "role_id")
    private Integer role_id;

    @Column(name = "name")
    private String name;

    @Column(name = "bonus")
    private float bonus;

    // Getters
    public Integer getRoleId() {
        return role_id;
    }

    public String getName() {
        return name;
    }

    public float getBonus() {
        return bonus;
    }

    // Setters
    public void setRoleId(Integer role_id) {
        this.role_id = role_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBonus(float bonus) {
        this.bonus = bonus;
    }
}
