package jay.dev.services;

import jay.dev.entities.LeaveBalance;
import jay.dev.entities.LeaveRequest;
import jay.dev.repositories.LeaveBalanceRepository;
import jay.dev.repositories.LeaveRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class LeaveRequestService {

    @Autowired
    private LeaveRequestRepository leaveRequestRepository;

    @Autowired
    private LeaveBalanceRepository leaveBalanceRepository;

    public LeaveRequest createLeaveRequest(LeaveRequest leaveRequest) {
        return leaveRequestRepository.save(leaveRequest);
    }

    public List<LeaveRequest> getAllLeaveRequests() {
        return leaveRequestRepository.findAll();
    }

    public LeaveRequest updateLeaveRequestStatus(Long id, String status) {
        Optional<LeaveRequest> leaveRequestOptional = leaveRequestRepository.findById(id);
        if (leaveRequestOptional.isPresent()) {
            LeaveRequest leaveRequest = leaveRequestOptional.get();
            leaveRequest.setStatus(status);
            return leaveRequestRepository.save(leaveRequest);
        }
        return null; // Or throw exception
    }

    public LeaveBalance getLeaveBalance(Long userId) {
        return leaveBalanceRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Leave balance not found"));
    }

    public long countPendingLeaveRequests(Long userId) {
        return leaveRequestRepository.countByStatusAndUserId("Pending", userId);
    }

    public long countThisYearLeave(Long userId, String status) {
        return leaveRequestRepository.countByUserIdAndStatusAndStartDateAfter(userId, status, LocalDate.now().withDayOfYear(1));
    }

    public List<LeaveRequest> getAllEmployeeLeavesForCalendar() {
        return leaveRequestRepository.findAll();
    }

    public void exportLeaveDataToExcel() {
        // Implement export functionality (using libraries like Apache POI)
    }
}
