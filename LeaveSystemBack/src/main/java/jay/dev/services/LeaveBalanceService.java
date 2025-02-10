package jay.dev.services;

import jay.dev.entities.LeaveBalance;
import jay.dev.repositories.LeaveBalanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LeaveBalanceService {

    @Autowired
    private LeaveBalanceRepository leaveBalanceRepository;

    public LeaveBalance getLeaveBalance(Long userId) {
        return leaveBalanceRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Leave balance not found"));
    }
}
