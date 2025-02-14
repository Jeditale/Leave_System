package jay.dev.services;

import jay.dev.entities.LeaveBalance;
import jay.dev.repositories.LeaveBalanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeaveBalanceService {

    @Autowired
    private LeaveBalanceRepository leaveBalanceRepository;

    public LeaveBalance getLeaveBalanceByUserId(Long userId) {
        return leaveBalanceRepository.findByUserId(userId)
                .stream().findFirst().orElseThrow(() -> new RuntimeException("Leave balance for user with ID " + userId + " not found"));
    }


}
