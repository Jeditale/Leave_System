package jay.dev.repositories;

import jay.dev.entities.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {

    List<LeaveRequest> findByUserId(Long userId);

    List<LeaveRequest> findByStatus(String status);
    long countByUserIdAndStatus(Long userId, String status);
    long countByUserIdAndStatusAndStartDateAfter(Long userId, String status, LocalDate startDate);
    long countByStatusAndUserId(String status, Long userId);

    List<LeaveRequest> findByStartDateBetween(LocalDate startDate, LocalDate endDate);

}

