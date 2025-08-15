package com.vacationmanagement.dto;

import java.time.LocalDateTime;

import com.vacationmanagement.model.VacationRequest;

import jakarta.validation.constraints.NotNull;

public class VacationRequestDto {
    private Long id;
    
    @NotNull(message = "Author is required")
    private Long author;
    
    private String status;
    private Long resolvedBy;
    private LocalDateTime requestCreatedAt;
    
    @NotNull(message = "Vacation start date is required")
    private LocalDateTime vacationStartDate;
    
    @NotNull(message = "Vacation end date is required")
    private LocalDateTime vacationEndDate;

    public VacationRequestDto() {}

    public VacationRequestDto(VacationRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("VacationRequest cannot be null");
        }
        
        this.id = request.getId();
        this.author = request.getAuthor();
        this.status = request.getStatus() != null ? request.getStatus().name().toLowerCase() : "pending";
        this.resolvedBy = request.getResolvedBy();
        this.requestCreatedAt = request.getRequestCreatedAt();
        this.vacationStartDate = request.getVacationStartDate();
        this.vacationEndDate = request.getVacationEndDate();
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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
        return "VacationRequestDto{" +
                "id=" + id +
                ", author=" + author +
                ", status='" + status + '\'' +
                ", resolvedBy=" + resolvedBy +
                ", requestCreatedAt=" + requestCreatedAt +
                ", vacationStartDate=" + vacationStartDate +
                ", vacationEndDate=" + vacationEndDate +
                '}';
    }
}