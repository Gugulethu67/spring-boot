package com.vacationmanagement.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.vacationmanagement.model.VacationRequest;
import com.vacationmanagement.model.VacationStatus;

@Repository
public interface VacationRequestRepository extends JpaRepository<VacationRequest, Long> {
    

    List<VacationRequest> findByAuthor(Long author);

    @Query("SELECT vr FROM VacationRequest vr WHERE vr.status = 'APPROVED'")
    List<VacationRequest> findApprovedRequests();

    @Query("SELECT vr FROM VacationRequest vr WHERE vr.status = 'PENDING'")
    List<VacationRequest> findPendingRequests();

    @Query("SELECT vr FROM VacationRequest vr WHERE vr.status = 'REJECTED'")
    List<VacationRequest> findRejectedRequests();

    @Query(value = "SELECT COALESCE(SUM(DATEDIFF('DAY', vacation_start_date, vacation_end_date) + 1), 0) " +
           "FROM vacation_requests " +
           "WHERE author = ?1 AND status = 'APPROVED'", nativeQuery = true)
    Integer findApprovedVacationDaysByAuthor(Long author);

    List<VacationRequest> findByStatus(VacationStatus status);
    List<VacationRequest> findByStatusIn(List<VacationStatus> statuses);
    List<VacationRequest> findByAuthorOrderByRequestCreatedAtDesc(Long authorId);
    List<VacationRequest> findByVacationStartDateBetweenOrVacationEndDateBetween(
        LocalDateTime start1, LocalDateTime end1,
        LocalDateTime start2, LocalDateTime end2
    );
}
