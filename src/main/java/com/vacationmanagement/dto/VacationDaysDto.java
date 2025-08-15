package com.vacationmanagement.dto;

public class VacationDaysDto {
    private Long employeeId;
    private Integer totalVacationDays;
    private Integer usedVacationDays;
    private Integer remainingVacationDays;


    public VacationDaysDto() {}


    public VacationDaysDto(Long employeeId, Integer totalVacationDays, Integer usedVacationDays, Integer remainingVacationDays) {
        this.employeeId = employeeId;
        this.totalVacationDays = totalVacationDays;
        this.usedVacationDays = usedVacationDays;
        this.remainingVacationDays = remainingVacationDays;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public Integer getTotalVacationDays() {
        return totalVacationDays;
    }

    public void setTotalVacationDays(Integer totalVacationDays) {
        this.totalVacationDays = totalVacationDays;
    }

    public Integer getUsedVacationDays() {
        return usedVacationDays;
    }

    public void setUsedVacationDays(Integer usedVacationDays) {
        this.usedVacationDays = usedVacationDays;
    }

    public Integer getRemainingVacationDays() {
        return remainingVacationDays;
    }

    public void setRemainingVacationDays(Integer remainingVacationDays) {
        this.remainingVacationDays = remainingVacationDays;
    }

    @Override
    public String toString() {
        return "VacationDaysDto{" +
                "employeeId=" + employeeId +
                ", totalVacationDays=" + totalVacationDays +
                ", usedVacationDays=" + usedVacationDays +
                ", remainingVacationDays=" + remainingVacationDays +
                '}';
    }
}