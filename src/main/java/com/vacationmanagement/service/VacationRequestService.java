package com.vacationmanagement.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vacationmanagement.dto.EmployeeOverviewDto;
import com.vacationmanagement.dto.VacationDaysDto;
import com.vacationmanagement.dto.VacationRequestDto;
import com.vacationmanagement.model.Employee;
import com.vacationmanagement.model.VacationRequest;
import com.vacationmanagement.model.VacationStatus;
import com.vacationmanagement.repository.EmployeeRepository;
import com.vacationmanagement.repository.VacationRequestRepository;

@Service
@Transactional
public class VacationRequestService {

    @Autowired
    private VacationRequestRepository vacationRequestRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    public List<VacationRequestDto> getRequestsByEmployee(Long employeeId) {
        List<VacationRequest> requests = vacationRequestRepository.findByAuthor(employeeId);
        return requests.stream()
            .map(VacationRequestDto::new)
                .collect(Collectors.toList());
    }

    public List<VacationRequestDto> getRequestsByEmployeeAndStatus(Long employeeId, String status) {
        VacationStatus requestStatus;
        try {
            requestStatus = VacationStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status: " + status);
        }

        List<VacationRequest> allRequests = vacationRequestRepository.findByAuthor(employeeId);
        List<VacationRequest> filteredRequests = allRequests.stream()
            .filter(request -> request.getStatus() == requestStatus)
            .collect(Collectors.toList());
                
        return filteredRequests.stream()
            .map(VacationRequestDto::new)
            .collect(Collectors.toList());
    }

    public VacationDaysDto getRemainingVacationDays(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
            .orElseThrow(() -> new RuntimeException("Employee not found"));

        List<VacationRequest> approvedRequests = vacationRequestRepository.findByAuthor(employeeId)
            .stream()
            .filter(request -> request.getStatus() == VacationStatus.APPROVED)
            .collect(Collectors.toList());

        int approvedDays = approvedRequests.stream()
                .mapToInt(request -> {
                    return (int) java.time.temporal.ChronoUnit.DAYS.between(
                            request.getVacationStartDate().toLocalDate(),
                            request.getVacationEndDate().toLocalDate()
                    ) + 1;
                })
                .sum();

        employee.setVacationDaysUsed(approvedDays);
        employeeRepository.save(employee);

        return new VacationDaysDto(
                employeeId,
                employee.getTotalVacationDays(),
                approvedDays,
                employee.getRemainingVacationDays()
        );
    }

    public VacationRequestDto createRequest(VacationRequestDto requestDto) {
        Employee employee = employeeRepository.findById(requestDto.getAuthor())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        if (requestDto.getVacationStartDate().isAfter(requestDto.getVacationEndDate())) {
            throw new RuntimeException("Start date must be before end date");
        }
        if (requestDto.getVacationStartDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Cannot request vacation for past dates");
        }

        
        long requestedDays = java.time.temporal.ChronoUnit.DAYS.between(
                requestDto.getVacationStartDate().toLocalDate(),
                requestDto.getVacationEndDate().toLocalDate()
        ) + 1;
        List<VacationRequest> approvedRequests = vacationRequestRepository.findByAuthor(requestDto.getAuthor())
            .stream()
            .filter(request -> request.getStatus() == VacationStatus.APPROVED)
            .collect(Collectors.toList());

        int approvedDays = approvedRequests.stream()
                .mapToInt(request -> {
                    return (int) java.time.temporal.ChronoUnit.DAYS.between(
                            request.getVacationStartDate().toLocalDate(),
                            request.getVacationEndDate().toLocalDate()
                    ) + 1;
                })
                .sum();

        int remainingDays = employee.getTotalVacationDays() - approvedDays;
        
        if (requestedDays > remainingDays) {
            throw new RuntimeException("Insufficient vacation days remaining");
        }

        VacationRequest request = new VacationRequest(
                requestDto.getAuthor(),
                requestDto.getVacationStartDate(),
                requestDto.getVacationEndDate()
        );

        VacationRequest savedRequest = vacationRequestRepository.save(request);
        return new VacationRequestDto(savedRequest);
    }

    public List<VacationRequestDto> getAllRequests() {
        List<VacationRequest> requests = vacationRequestRepository.findAll();
        return requests.stream()
            .map(VacationRequestDto::new)
            .collect(Collectors.toList());
    }

    public VacationRequestDto updateRequestStatus(Long requestId, String status, Long managerId) {
        VacationRequest request = vacationRequestRepository.findById(requestId)
            .orElseThrow(() -> new RuntimeException("Request not found"));

        Employee manager = employeeRepository.findById(managerId)
            .orElseThrow(() -> new RuntimeException("Manager not found"));

        if (!manager.getIsManager()) {
            throw new RuntimeException("Only managers can approve/reject requests");
        }

        VacationStatus requestStatus;
        try {
            requestStatus = VacationStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status: " + status);
        }

        request.setStatus(requestStatus);
        request.setResolvedBy(managerId);

        VacationRequest savedRequest = vacationRequestRepository.save(request);
        return new VacationRequestDto(savedRequest);
    }

    

    public List<VacationRequestDto> getRequestsByStatus(String status) {
        try {
            VacationStatus requestStatus = VacationStatus.valueOf(status.toUpperCase());
            List<VacationRequest> requests = vacationRequestRepository.findByStatus(requestStatus);
            return requests.stream()
                .map(VacationRequestDto::new)
                .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status: " + status + ". Valid values are: PENDING, APPROVED, REJECTED");
        }
    }

    public List<VacationRequestDto> getRequestsByMultipleStatuses(List<String> statuses) {
        try {
            List<VacationStatus> requestStatuses = statuses.stream()
                .map(status -> VacationStatus.valueOf(status.toUpperCase()))
                .collect(Collectors.toList());
            
            List<VacationRequest> requests = vacationRequestRepository.findByStatusIn(requestStatuses);
            return requests.stream()
                .map(VacationRequestDto::new)
                .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status provided. Valid values are: PENDING, APPROVED, REJECTED");
        }
    }

    public List<VacationRequestDto> getOverlappingRequests() {
        List<VacationRequest> allApprovedRequests = vacationRequestRepository.findByStatus(VacationStatus.APPROVED);
        List<VacationRequest> overlappingRequests = new ArrayList<>();
        
        for (int i = 0; i < allApprovedRequests.size(); i++) {
            VacationRequest request1 = allApprovedRequests.get(i);
            
            for (int j = i + 1; j < allApprovedRequests.size(); j++) {
                VacationRequest request2 = allApprovedRequests.get(j);
                
                if (datesOverlap(request1.getVacationStartDate(), request1.getVacationEndDate(),
                            request2.getVacationStartDate(), request2.getVacationEndDate())) {
                    
                    if (!overlappingRequests.contains(request1)) {
                        overlappingRequests.add(request1);
                    }
                    if (!overlappingRequests.contains(request2)) {
                        overlappingRequests.add(request2);
                    }
                }
            }
        }
        
        return overlappingRequests.stream()
                .map(VacationRequestDto::new)
                .sorted(Comparator.comparing(VacationRequestDto::getVacationStartDate))
                .collect(Collectors.toList());
    }

    private boolean datesOverlap(LocalDateTime start1, LocalDateTime end1, 
                            LocalDateTime start2, LocalDateTime end2) {
        return start1.isBefore(end2) && start2.isBefore(end1);
    }


    public EmployeeOverviewDto getEmployeeOverview(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + employeeId));
        
        List<VacationRequest> allRequests = vacationRequestRepository.findByAuthorOrderByRequestCreatedAtDesc(employeeId);
        List<VacationRequestDto> requestDtos = allRequests.stream()
                .map(VacationRequestDto::new)
                .collect(Collectors.toList());
        
        Map<VacationStatus, Long> statusCounts = allRequests.stream()
                .collect(Collectors.groupingBy(VacationRequest::getStatus, Collectors.counting()));
        
        int pendingCount = statusCounts.getOrDefault(VacationStatus.PENDING, 0L).intValue();
        int approvedCount = statusCounts.getOrDefault(VacationStatus.APPROVED, 0L).intValue();
        int rejectedCount = statusCounts.getOrDefault(VacationStatus.REJECTED, 0L).intValue();
        
        int remainingDays = employee.getTotalVacationDays() - employee.getVacationDaysUsed();
        
        return new EmployeeOverviewDto(
                employee.getId(),
                employee.getName(),
                employee.getEmail(),
                employee.getIsManager(),
                employee.getTotalVacationDays(),
                employee.getVacationDaysUsed(),
                remainingDays,
                requestDtos,
                pendingCount,
                approvedCount,
                rejectedCount,
                allRequests.size()
        );
    }


    public List<VacationRequestDto> getRequestsByDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date must be before or equal to end date");
        }
        
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);
        
        List<VacationRequest> requests = vacationRequestRepository.findByVacationStartDateBetweenOrVacationEndDateBetween(
                startDateTime, endDateTime, startDateTime, endDateTime);
        
        return requests.stream()
            .map(VacationRequestDto::new)
            .sorted(Comparator.comparing(VacationRequestDto::getVacationStartDate))
            .collect(Collectors.toList());
    }

    public Map<String, Object> getRequestsSummary() {
        List<VacationRequest> allRequests = vacationRequestRepository.findAll();
        List<Employee> allEmployees = employeeRepository.findAll();

        Map<VacationStatus, Long> statusCounts = allRequests.stream()
            .collect(Collectors.groupingBy(VacationRequest::getStatus, Collectors.counting()));
        
        int totalVacationDaysUsed = allEmployees.stream()
            .mapToInt(Employee::getVacationDaysUsed)
            .sum();
        
        int totalVacationDaysAvailable = allEmployees.stream()
            .mapToInt(Employee::getTotalVacationDays)
            .sum();
        
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime thirtyDaysFromNow = now.plusDays(30);
        
        long upcomingVacationsCount = allRequests.stream()
            .filter(req -> req.getStatus() == VacationStatus.APPROVED)
            .filter(req -> req.getVacationStartDate().isAfter(now) && 
                    req.getVacationStartDate().isBefore(thirtyDaysFromNow))
            .count();
        
        Map<String, Object> summary = new HashMap<>();
        summary.put("totalRequests", allRequests.size());
        summary.put("pendingRequests", statusCounts.getOrDefault(VacationStatus.PENDING, 0L));
        summary.put("approvedRequests", statusCounts.getOrDefault(VacationStatus.APPROVED, 0L));
        summary.put("rejectedRequests", statusCounts.getOrDefault(VacationStatus.REJECTED, 0L));
        summary.put("totalEmployees", allEmployees.size());
        summary.put("totalVacationDaysUsed", totalVacationDaysUsed);
        summary.put("totalVacationDaysAvailable", totalVacationDaysAvailable);
        summary.put("totalVacationDaysRemaining", totalVacationDaysAvailable - totalVacationDaysUsed);
        summary.put("upcomingVacationsNext30Days", upcomingVacationsCount);
        summary.put("lastUpdated", LocalDateTime.now());
        
        return summary;
    }
}