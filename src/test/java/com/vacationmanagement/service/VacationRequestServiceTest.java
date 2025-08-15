package com.vacationmanagement.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.vacationmanagement.dto.VacationDaysDto;
import com.vacationmanagement.dto.VacationRequestDto;
import com.vacationmanagement.model.Employee;
import com.vacationmanagement.model.VacationRequest;
import com.vacationmanagement.model.VacationStatus;
import com.vacationmanagement.repository.EmployeeRepository;
import com.vacationmanagement.repository.VacationRequestRepository;

@ExtendWith(MockitoExtension.class)
class VacationRequestServiceTest {

    @Mock
    private VacationRequestRepository vacationRequestRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private VacationRequestService vacationRequestService;

    private Employee testEmployee;
    private Employee testManager;
    private VacationRequest testRequest;

    @BeforeEach
    void setUp() {
        testEmployee = new Employee("John Doe", "john@example.com", false);
        testEmployee.setId(1L);
        testEmployee.setTotalVacationDays(25);
        testEmployee.setVacationDaysUsed(5);

        testManager = new Employee("Jane Manager", "jane@example.com", true);
        testManager.setId(2L);
        testRequest = new VacationRequest();
        testRequest.setId(1L);
        testRequest.setAuthor(1L);
        testRequest.setStatus(VacationStatus.PENDING);
        testRequest.setRequestCreatedAt(LocalDateTime.now());
        testRequest.setVacationStartDate(LocalDateTime.of(2025, 9, 1, 0, 0));
        testRequest.setVacationEndDate(LocalDateTime.of(2025, 9, 5, 0, 0));
    }

    @Test
    void testVacationRequestDto_Constructor() {
        VacationRequest request = new VacationRequest();
        request.setId(1L);
        request.setAuthor(1L);
        request.setStatus(VacationStatus.PENDING);
        request.setRequestCreatedAt(LocalDateTime.now());
        request.setVacationStartDate(LocalDateTime.of(2025, 9, 1, 0, 0));
        request.setVacationEndDate(LocalDateTime.of(2025, 9, 5, 0, 0));

        VacationRequestDto dto = new VacationRequestDto(request);

        assertEquals(1L, dto.getId());
        assertEquals(1L, dto.getAuthor());
        assertEquals("pending", dto.getStatus());
        assertNotNull(dto.getRequestCreatedAt());
    }

    @Test
    void testGetRequestsByEmployee_ReturnsEmployeeRequests() {
        VacationRequest mockRequest = new VacationRequest();
        mockRequest.setId(1L);
        mockRequest.setAuthor(1L);
        mockRequest.setStatus(VacationStatus.PENDING);
        mockRequest.setRequestCreatedAt(LocalDateTime.now());
        mockRequest.setVacationStartDate(LocalDateTime.of(2025, 9, 1, 0, 0));
        mockRequest.setVacationEndDate(LocalDateTime.of(2025, 9, 5, 0, 0));
        
        List<VacationRequest> requests = Arrays.asList(mockRequest);
        when(vacationRequestRepository.findByAuthor(1L)).thenReturn(requests);


        List<VacationRequestDto> result = vacationRequestService.getRequestsByEmployee(1L);

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getAuthor());
        assertEquals("pending", result.get(0).getStatus());
        assertNotNull(result.get(0).getId());
        verify(vacationRequestRepository).findByAuthor(1L);
    }

    @Test
    void testGetRemainingVacationDays_ReturnsCorrectCalculation() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(testEmployee));
        when(vacationRequestRepository.findByAuthor(1L)).thenReturn(Arrays.asList());

        VacationDaysDto result = vacationRequestService.getRemainingVacationDays(1L);

        assertEquals(25, result.getTotalVacationDays());
        assertEquals(0, result.getUsedVacationDays());
        assertEquals(25, result.getRemainingVacationDays());
    }

    @Test
    void testCreateRequest_PastDate_ThrowsException() {
        VacationRequestDto requestDto = new VacationRequestDto();
        requestDto.setAuthor(1L);
        requestDto.setVacationStartDate(LocalDateTime.of(2024, 1, 1, 0, 0)); // Past date
        requestDto.setVacationEndDate(LocalDateTime.of(2024, 1, 3, 0, 0));

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(testEmployee));

        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> vacationRequestService.createRequest(requestDto));
        assertEquals("Cannot request vacation for past dates", exception.getMessage());
    }

    @Test
    void testUpdateRequestStatus_ValidManagerApproval_Success() {
        when(vacationRequestRepository.findById(1L)).thenReturn(Optional.of(testRequest));
        when(employeeRepository.findById(2L)).thenReturn(Optional.of(testManager));
        when(vacationRequestRepository.save(any(VacationRequest.class))).thenReturn(testRequest);

        VacationRequestDto result = vacationRequestService.updateRequestStatus(1L, "APPROVED", 2L);

        assertEquals("approved", result.getStatus());
        assertEquals(2L, result.getResolvedBy());
    }

    @Test
    void testUpdateRequestStatus_NonManagerUser_ThrowsException() {
        when(vacationRequestRepository.findById(1L)).thenReturn(Optional.of(testRequest));
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(testEmployee)); // Not a manager

        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> vacationRequestService.updateRequestStatus(1L, "APPROVED", 1L));
        assertEquals("Only managers can approve/reject requests", exception.getMessage());
    }

    @Test
    void testGetRequestsByStatus_ValidStatus_ReturnsFilteredRequests() {

        List<VacationRequest> pendingRequests = Arrays.asList(testRequest);
        when(vacationRequestRepository.findByStatus(VacationStatus.PENDING)).thenReturn(pendingRequests);

        List<VacationRequestDto> result = vacationRequestService.getRequestsByStatus("PENDING");

        assertEquals(1, result.size());
        assertEquals("pending", result.get(0).getStatus());
    }

    @Test
    void testGetRequestsByStatus_InvalidStatus_ThrowsException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> vacationRequestService.getRequestsByStatus("INVALID"));
        assertTrue(exception.getMessage().contains("Invalid status: INVALID"));
    }

    @Test
    void testGetOverlappingRequests_FindsOverlaps() {
        VacationRequest request1 = new VacationRequest(1L, 
            LocalDateTime.of(2025, 9, 1, 0, 0), 
            LocalDateTime.of(2025, 9, 5, 0, 0));
        VacationRequest request2 = new VacationRequest(2L, 
            LocalDateTime.of(2025, 9, 3, 0, 0), 
            LocalDateTime.of(2025, 9, 7, 0, 0));
        
        List<VacationRequest> approvedRequests = Arrays.asList(request1, request2);
        when(vacationRequestRepository.findByStatus(VacationStatus.APPROVED)).thenReturn(approvedRequests);

        List<VacationRequestDto> result = vacationRequestService.getOverlappingRequests();

        assertEquals(2, result.size()); // Both requests should be returned as overlapping
    }
}