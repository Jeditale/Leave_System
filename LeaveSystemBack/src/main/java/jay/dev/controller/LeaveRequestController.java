package jay.dev.controller;
import jay.dev.entities.LeaveBalance;
import jay.dev.entities.LeaveRequest;
import jay.dev.services.LeaveBalanceService;
import jay.dev.services.LeaveRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.persistence.EntityNotFoundException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/leave-requests")
public class LeaveRequestController {
    @Autowired
    private LeaveRequestService leaveRequestService;
    @Autowired
    private LeaveBalanceService leaveBalanceService;
    @PostMapping
    public ResponseEntity<LeaveRequest> createLeaveRequest(@RequestBody LeaveRequest leaveRequest) {
        LeaveRequest createdLeaveRequest = leaveRequestService.createLeaveRequest(leaveRequest);
        return ResponseEntity.ok(createdLeaveRequest);
    }

    @GetMapping
    public ResponseEntity<List<LeaveRequest>> getAllLeaveRequests() {
        return ResponseEntity.ok(leaveRequestService.getAllLeaveRequests());
    }

    //removed because it was only uses to debug and edit data(which now i used dbeaver instead)
//    @PutMapping("/{id}")
//    public ResponseEntity<LeaveRequest> updateLeaveRequestStatus(@PathVariable Long id, @RequestParam String status) {
//        LeaveRequest updatedLeaveRequest = leaveRequestService.updateLeaveRequestStatus(id, status);
//        return ResponseEntity.ok(updatedLeaveRequest);
//    }

    @GetMapping("/balance/{userId}")
    public ResponseEntity<Integer> getRemainingLeaveDays(@PathVariable Long userId) {
        LeaveBalance leaveBalance = leaveBalanceService.getLeaveBalanceByUserId(userId);
        return ResponseEntity.ok(leaveBalance.getRemainingDays());
    }
    @GetMapping("/pending/count/{userId}")
    public ResponseEntity<Long> getPendingLeaveCount(@PathVariable Long userId) {
        long count = leaveRequestService.getPendingLeaveCount(userId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count-pending/{userId}")
    public ResponseEntity<Long> countPendingLeaveRequests(@PathVariable Long userId) {
        return ResponseEntity.ok(leaveRequestService.countPendingLeaveRequests(userId));
    }

    @GetMapping("/count-this-year/{userId}")
    public ResponseEntity<Long> countThisYearLeave(@PathVariable Long userId) {
        return ResponseEntity.ok(leaveRequestService.countThisYearLeave(userId, "Approved"));
    }

//    @GetMapping("/calendar")
//    public ResponseEntity<List<LeaveRequest>> getAllEmployeeLeavesForCalendar() {
//        return ResponseEntity.ok(leaveRequestService.getAllEmployeeLeavesForCalendar());
//    }

    // Endpoint to get all pending leave requests
    @GetMapping("/pending")
    public List<LeaveRequest> getPendingRequests() {
        return leaveRequestService.getPendingRequests();
    }

    // Endpoint to approve a leave request
    @PostMapping("/approve/{id}")
    public ResponseEntity<String> approveLeave(@PathVariable Long id, @RequestBody LeaveRequest leaveRequest) {
        try {
            leaveRequestService.approveLeave(id, leaveRequest);
            return ResponseEntity.ok("Leave request approved successfully");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body("Leave request not found");
        }
    }

    // Endpoint to reject a leave request
    @PostMapping("/reject/{id}")
    public ResponseEntity<String> rejectLeave(@PathVariable Long id, @RequestBody LeaveRequest leaveRequest) {
        try {
            leaveRequestService.rejectLeave(id, leaveRequest);
            return ResponseEntity.ok("Leave request rejected");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body("Leave request not found");
        }
    }

    @GetMapping("/stats/{year}/{month}")
    public ResponseEntity<Map<String, Integer>> getLeaveStats(
            @PathVariable int year, @PathVariable int month) {
        Map<String, Integer> stats = leaveRequestService.getLeaveStats(year, month);
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/export/{year}/{month}")
    public ResponseEntity<String> exportLeaveDataToExcel(
            @PathVariable int year, @PathVariable int month) {
        try {
            String base64Excel = leaveRequestService.exportLeaveDataToExcel(year, month);
            return ResponseEntity.ok(base64Excel);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error generating Excel file");
        }
    }
    @GetMapping("/user/{userId}")
    public List<LeaveRequest> getLeavesByUserId(@PathVariable Long userId) {
        return leaveRequestService.getLeaveRequestsByUserId(userId);
    }

    @GetMapping("/stats/{userId}/{month}/{year}")
    public Map<String, Integer> getLeaveStats(
            @PathVariable Long userId,
            @PathVariable int month,
            @PathVariable int year) {
        return leaveRequestService.getApprovedLeaveStats(userId, month, year);
    }
}
