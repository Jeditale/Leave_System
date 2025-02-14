package jay.dev.repositories;
import jay.dev.entities.LeaveBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaveBalanceRepository extends JpaRepository<LeaveBalance, Long> {
    @Query("SELECT lb FROM LeaveBalance lb WHERE lb.user.id =:userId")
    List<LeaveBalance>findByUserId(@Param("userId")Long userId);
}
