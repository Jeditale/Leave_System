package jay.dev.controller;

import jay.dev.entities.LeaveBalance;
import jay.dev.entities.LeaveRequest;
import jay.dev.services.LeaveRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leave-requests")
public class LeaveRequestController {

    @Autowired
    private LeaveRequestService leaveRequestService;

    @PostMapping
    public ResponseEntity<LeaveRequest> createLeaveRequest(@RequestBody LeaveRequest leaveRequest) {
        LeaveRequest createdLeaveRequest = leaveRequestService.createLeaveRequest(leaveRequest);
        return ResponseEntity.ok(createdLeaveRequest);
    }

    @GetMapping
    public ResponseEntity<List<LeaveRequest>> getAllLeaveRequests() {
        return ResponseEntity.ok(leaveRequestService.getAllLeaveRequests());
    }

    @PutMapping("/{id}")
    public ResponseEntity<LeaveRequest> updateLeaveRequestStatus(@PathVariable Long id, @RequestParam String status) {
        LeaveRequest updatedLeaveRequest = leaveRequestService.updateLeaveRequestStatus(id, status);
        return ResponseEntity.ok(updatedLeaveRequest);
    }

    @GetMapping("/balance/{userId}")
    public ResponseEntity<LeaveBalance> getLeaveBalance(@PathVariable Long userId) {
        return ResponseEntity.ok(leaveRequestService.getLeaveBalance(userId));
    }

    @GetMapping("/count-pending/{userId}")
    public ResponseEntity<Long> countPendingLeaveRequests(@PathVariable Long userId) {
        return ResponseEntity.ok(leaveRequestService.countPendingLeaveRequests(userId));
    }

    @GetMapping("/count-this-year/{userId}")
    public ResponseEntity<Long> countThisYearLeave(@PathVariable Long userId) {
        return ResponseEntity.ok(leaveRequestService.countThisYearLeave(userId, "Approved"));
    }

    @GetMapping("/calendar")
    public ResponseEntity<List<LeaveRequest>> getAllEmployeeLeavesForCalendar() {
        return ResponseEntity.ok(leaveRequestService.getAllEmployeeLeavesForCalendar());
    }

    @GetMapping("/export")
    public void exportLeaveData() {
        leaveRequestService.exportLeaveDataToExcel();
    }
}
