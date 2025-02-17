package jay.dev.repositories;
import jay.dev.entities.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {

    List<LeaveRequest> findByUserId(Long userId);

    List<LeaveRequest> findByStatus(String status);

    long countByUserIdAndStatusAndStartDateAfter(Long userId, String status, LocalDate startDate);

    long countByStatusAndUserId(String status, Long userId);

    List<LeaveRequest> findByStartDateBetween(LocalDate startDate, LocalDate endDate);

    @Query("SELECT COUNT(l) FROM LeaveRequest l WHERE l.user.id = :userId AND l.status = 'รออนุมัติ'")
    long countPendingLeavesByUserId(@Param("userId") Long userId);

    List<LeaveRequest> findByStartDateBetweenAndStatus(LocalDate startDate, LocalDate endDate, String status);

}

