package jay.dev.repositories;

import jay.dev.entities.LeaveBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface LeaveBalanceRepository extends JpaRepository<LeaveBalance, Long> {

    Optional<LeaveBalance> findByUserIdAndLeaveTypeId(Long userId, Long leaveTypeId);

    Optional<LeaveBalance> findByUserId(Long userId);
}
