package com.vacationmanagement.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "vacation_requests")
public class VacationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Long author;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VacationStatus status = VacationStatus.PENDING;
    
    private Long resolvedBy;
    
    @Column(nullable = false)
    private LocalDateTime requestCreatedAt = LocalDateTime.now();
    
    @Column(nullable = false)
    private LocalDateTime vacationStartDate;
    
    @Column(nullable = false)
    private LocalDateTime vacationEndDate;


    public VacationRequest() {}


    public VacationRequest(Long author, LocalDateTime vacationStartDate, LocalDateTime vacationEndDate) {
        this.author = author;
        this.vacationStartDate = vacationStartDate;
        this.vacationEndDate = vacationEndDate;
        this.requestCreatedAt = LocalDateTime.now();
        this.status = VacationStatus.PENDING;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAuthor() {
        return author;
    }

    public void setAuthor(Long author) {
        this.author = author;
    }

    public VacationStatus getStatus() {
        return status;
    }

    public void setStatus(VacationStatus status) {
        this.status = status;
    }

    public Long getResolvedBy() {
        return resolvedBy;
    }

    public void setResolvedBy(Long resolvedBy) {
        this.resolvedBy = resolvedBy;
    }

    public LocalDateTime getRequestCreatedAt() {
        return requestCreatedAt;
    }

    public void setRequestCreatedAt(LocalDateTime requestCreatedAt) {
        this.requestCreatedAt = requestCreatedAt;
    }

    public LocalDateTime getVacationStartDate() {
        return vacationStartDate;
    }

    public void setVacationStartDate(LocalDateTime vacationStartDate) {
        this.vacationStartDate = vacationStartDate;
    }

    public LocalDateTime getVacationEndDate() {
        return vacationEndDate;
    }

    public void setVacationEndDate(LocalDateTime vacationEndDate) {
        this.vacationEndDate = vacationEndDate;
    }

    @Override
    public String toString() {
        return "VacationRequest{" +
                "id=" + id +
                ", author=" + author +
                ", status=" + status +
                ", resolvedBy=" + resolvedBy +
                ", requestCreatedAt=" + requestCreatedAt +
                ", vacationStartDate=" + vacationStartDate +
                ", vacationEndDate=" + vacationEndDate +
                '}';
    }
}