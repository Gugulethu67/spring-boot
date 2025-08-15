package com.vacationmanagement.dto;

import java.util.List;

public class EmployeeOverviewDto {
    
    private Long employeeId;
    private String employeeName;
    private String email;
    private Boolean isManager;
    private Integer totalVacationDays;
    private Integer vacationDaysUsed;
    private Integer remainingVacationDays;
    private List<VacationRequestDto> allRequests;
    private Integer pendingRequestsCount;
    private Integer approvedRequestsCount;
    private Integer rejectedRequestsCount;
    private Integer totalRequestsCount;


    public EmployeeOverviewDto() {}

    public EmployeeOverviewDto(Long employeeId, String employeeName, String email, Boolean isManager,
                              Integer totalVacationDays, Integer vacationDaysUsed, Integer remainingVacationDays,
                              List<VacationRequestDto> allRequests, Integer pendingRequestsCount,
                              Integer approvedRequestsCount, Integer rejectedRequestsCount, Integer totalRequestsCount) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.email = email;
        this.isManager = isManager;
        this.totalVacationDays = totalVacationDays;
        this.vacationDaysUsed = vacationDaysUsed;
        this.remainingVacationDays = remainingVacationDays;
        this.allRequests = allRequests;
        this.pendingRequestsCount = pendingRequestsCount;
        this.approvedRequestsCount = approvedRequestsCount;
        this.rejectedRequestsCount = rejectedRequestsCount;
        this.totalRequestsCount = totalRequestsCount;
    }


    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
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
        return remainingVacationDays;
    }

    public void setRemainingVacationDays(Integer remainingVacationDays) {
        this.remainingVacationDays = remainingVacationDays;
    }

    public List<VacationRequestDto> getAllRequests() {
        return allRequests;
    }

    public void setAllRequests(List<VacationRequestDto> allRequests) {
        this.allRequests = allRequests;
    }

    public Integer getPendingRequestsCount() {
        return pendingRequestsCount;
    }

    public void setPendingRequestsCount(Integer pendingRequestsCount) {
        this.pendingRequestsCount = pendingRequestsCount;
    }

    public Integer getApprovedRequestsCount() {
        return approvedRequestsCount;
    }

    public void setApprovedRequestsCount(Integer approvedRequestsCount) {
        this.approvedRequestsCount = approvedRequestsCount;
    }

    public Integer getRejectedRequestsCount() {
        return rejectedRequestsCount;
    }

    public void setRejectedRequestsCount(Integer rejectedRequestsCount) {
        this.rejectedRequestsCount = rejectedRequestsCount;
    }

    public Integer getTotalRequestsCount() {
        return totalRequestsCount;
    }

    public void setTotalRequestsCount(Integer totalRequestsCount) {
        this.totalRequestsCount = totalRequestsCount;
    }

    @Override
    public String toString() {
        return "EmployeeOverviewDto{" +
                "employeeId=" + employeeId +
                ", employeeName='" + employeeName + '\'' +
                ", email='" + email + '\'' +
                ", isManager=" + isManager +
                ", totalVacationDays=" + totalVacationDays +
                ", vacationDaysUsed=" + vacationDaysUsed +
                ", remainingVacationDays=" + remainingVacationDays +
                ", pendingRequestsCount=" + pendingRequestsCount +
                ", approvedRequestsCount=" + approvedRequestsCount +
                ", rejectedRequestsCount=" + rejectedRequestsCount +
                ", totalRequestsCount=" + totalRequestsCount +
                '}';
    }
}