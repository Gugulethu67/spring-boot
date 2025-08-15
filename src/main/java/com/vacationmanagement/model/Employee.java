package com.vacationmanagement.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private Boolean isManager = false;
    
    @Column(nullable = false)
    private Integer totalVacationDays = 25; 
    
    @Column(nullable = false)
    private Integer vacationDaysUsed = 0;


    public Employee() {}


    public Employee(String name, String email, Boolean isManager) {
        this.name = name;
        this.email = email;
        this.isManager = isManager != null ? isManager : false;
        this.totalVacationDays = 30;
        this.vacationDaysUsed = 0;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getIsManager() {
        return isManager;
    }

    public void setIsManager(Boolean isManager) {
        this.isManager = isManager;
    }

    public Integer getTotalVacationDays() {
        return totalVacationDays;
    }

    public void setTotalVacationDays(Integer totalVacationDays) {
        this.totalVacationDays = totalVacationDays;
    }

    public Integer getVacationDaysUsed() {
        return vacationDaysUsed;
    }

    public void setVacationDaysUsed(Integer vacationDaysUsed) {
        this.vacationDaysUsed = vacationDaysUsed;
    }

    public Integer getRemainingVacationDays() {
        return this.totalVacationDays - this.vacationDaysUsed;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", isManager=" + isManager +
                ", totalVacationDays=" + totalVacationDays +
                ", vacationDaysUsed=" + vacationDaysUsed +
                '}';
    }
}