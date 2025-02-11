package jay.dev.services;

import jakarta.persistence.EntityNotFoundException;
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
    // Get all pending leave requests
    public List<LeaveRequest> getPendingRequests() {
        return leaveRequestRepository.findByStatus("Pending");
    }

    // Approve a leave request
    public void approveLeave(Long id, LeaveRequest leaveRequest) {
        LeaveRequest request = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("LeaveRequest with id " + id + " not found"));
        request.setStatus("Approved");
        request.setComment(leaveRequest.getComment()); // Store comment
        leaveRequestRepository.save(request);
    }

    // Reject a leave request
    public void rejectLeave(Long id, LeaveRequest leaveRequest) {
        LeaveRequest request = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("LeaveRequest with id " + id + " not found"));
        request.setStatus("Rejected");
        request.setComment(leaveRequest.getComment()); // Store comment
        leaveRequestRepository.save(request);
    }
}
