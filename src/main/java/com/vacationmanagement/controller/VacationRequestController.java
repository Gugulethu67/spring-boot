package com.vacationmanagement.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vacationmanagement.dto.EmployeeOverviewDto;
import com.vacationmanagement.dto.VacationDaysDto;
import com.vacationmanagement.dto.VacationRequestDto;
import com.vacationmanagement.service.VacationRequestService;

@RestController
@RequestMapping("/api/vacation-requests")
@CrossOrigin(origins = "*")
public class VacationRequestController {

    @Autowired
    private VacationRequestService vacationRequestService;

    @GetMapping
    public ResponseEntity<List<VacationRequestDto>> getAllVacationRequests() {
        List<VacationRequestDto> requests = vacationRequestService.getAllRequests();
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<VacationRequestDto>> getVacationRequestsByEmployee(@PathVariable Long employeeId) {
        List<VacationRequestDto> requests = vacationRequestService.getRequestsByEmployee(employeeId);
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/employee/{employeeId}/status/{status}")
    public ResponseEntity<List<VacationRequestDto>> getVacationRequestsByEmployeeAndStatus(
            @PathVariable Long employeeId, 
            @PathVariable String status) {
        try {
            List<VacationRequestDto> requests = vacationRequestService.getRequestsByEmployeeAndStatus(employeeId, status);
            return ResponseEntity.ok(requests);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/employee/{employeeId}/remaining-days")
    public ResponseEntity<VacationDaysDto> getRemainingVacationDays(@PathVariable Long employeeId) {
        try {
            VacationDaysDto vacationDays = vacationRequestService.getRemainingVacationDays(employeeId);
            return ResponseEntity.ok(vacationDays);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<VacationRequestDto> createVacationRequest(@RequestBody VacationRequestDto requestDto) {
        try {
            VacationRequestDto createdRequest = vacationRequestService.createRequest(requestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdRequest);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/{requestId}/status")
    public ResponseEntity<VacationRequestDto> updateRequestStatus(
            @PathVariable Long requestId,
            @RequestBody Map<String, Object> request) {
        try {
            String status = (String) request.get("status");
            Long managerId = Long.valueOf(request.get("managerId").toString());
            
            VacationRequestDto updatedRequest = vacationRequestService.updateRequestStatus(requestId, status, managerId);
            return ResponseEntity.ok(updatedRequest);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

 
    @GetMapping("/status/{status}")
    public ResponseEntity<List<VacationRequestDto>> getRequestsByStatus(@PathVariable String status) {
        try {
            List<VacationRequestDto> requests = vacationRequestService.getRequestsByStatus(status);
            return ResponseEntity.ok(requests);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/overlapping")
    public ResponseEntity<List<VacationRequestDto>> getOverlappingRequests() {
        try {
            List<VacationRequestDto> overlappingRequests = vacationRequestService.getOverlappingRequests();
            return ResponseEntity.ok(overlappingRequests);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/employee/{employeeId}/overview")
    public ResponseEntity<EmployeeOverviewDto> getEmployeeOverview(@PathVariable Long employeeId) {
        try {
            EmployeeOverviewDto overview = vacationRequestService.getEmployeeOverview(employeeId);
            return ResponseEntity.ok(overview);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/status")
    public ResponseEntity<List<VacationRequestDto>> getRequestsByMultipleStatuses(
            @RequestParam("statuses") List<String> statuses) {
        try {
            List<VacationRequestDto> requests = vacationRequestService.getRequestsByMultipleStatuses(statuses);
            return ResponseEntity.ok(requests);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }


    @GetMapping("/date-range")
    public ResponseEntity<List<VacationRequestDto>> getRequestsByDateRange(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            List<VacationRequestDto> requests = vacationRequestService.getRequestsByDateRange(startDate, endDate);
            return ResponseEntity.ok(requests);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }


    @GetMapping("/summary")
    public ResponseEntity<Map<String, Object>> getRequestsSummary() {
        try {
            Map<String, Object> summary = vacationRequestService.getRequestsSummary();
            return ResponseEntity.ok(summary);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}