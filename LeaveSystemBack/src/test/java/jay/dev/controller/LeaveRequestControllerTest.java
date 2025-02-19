package jay.dev.controller;

import jay.dev.entities.LeaveBalance;
import jay.dev.entities.LeaveRequest;
import jay.dev.entities.LeaveType;
import jay.dev.services.LeaveBalanceService;
import jay.dev.services.LeaveRequestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class LeaveRequestControllerTest {

    @Mock
    private LeaveRequestService leaveRequestService;

    @Mock
    private LeaveBalanceService leaveBalanceService;

    @InjectMocks
    private LeaveRequestController leaveRequestController;

    private LeaveRequest sampleLeaveRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // Setup common test data
        sampleLeaveRequest = new LeaveRequest();
        sampleLeaveRequest.setId(1L);
        sampleLeaveRequest.setStatus("Pending");
        
        LeaveType leaveType = new LeaveType();
        leaveType.setName("Annual Leave");
        sampleLeaveRequest.setLeaveType(leaveType);
        
        LocalDate startDate = LocalDate.now();
        sampleLeaveRequest.setStartDate(startDate);
        sampleLeaveRequest.setEndDate(startDate.plusDays(5));
    }

    @Test
    void createLeaveRequest_ShouldReturnCreatedRequest() {
        when(leaveRequestService.createLeaveRequest(any(LeaveRequest.class))).thenReturn(sampleLeaveRequest);

        ResponseEntity<LeaveRequest> response = leaveRequestController.createLeaveRequest(sampleLeaveRequest);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(sampleLeaveRequest, response.getBody());
        verify(leaveRequestService).createLeaveRequest(sampleLeaveRequest);
    }

    @Test
    void getAllLeaveRequests_ShouldReturnListOfRequests() {
        List<LeaveRequest> requests = Arrays.asList(sampleLeaveRequest, new LeaveRequest());
        when(leaveRequestService.getAllLeaveRequests()).thenReturn(requests);

        ResponseEntity<List<LeaveRequest>> response = leaveRequestController.getAllLeaveRequests();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(requests, response.getBody());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void getRemainingLeaveDays_ShouldReturnBalance() {
        LeaveBalance balance = new LeaveBalance();
        balance.setRemainingDays(10);
        when(leaveBalanceService.getLeaveBalanceByUserId(1L)).thenReturn(balance);

        ResponseEntity<Integer> response = leaveRequestController.getRemainingLeaveDays(1L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(10, response.getBody());
        verify(leaveBalanceService).getLeaveBalanceByUserId(1L);
    }

    @Test
    void getPendingLeaveCount_ShouldReturnCount() {
        when(leaveRequestService.getPendingLeaveCount(1L)).thenReturn(5L);

        ResponseEntity<Long> response = leaveRequestController.getPendingLeaveCount(1L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(5L, response.getBody());
        verify(leaveRequestService).getPendingLeaveCount(1L);
    }

    @Test
    void countPendingLeaveRequests_ShouldReturnCount() {
        when(leaveRequestService.countPendingLeaveRequests(1L)).thenReturn(3L);

        ResponseEntity<Long> response = leaveRequestController.countPendingLeaveRequests(1L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(3L, response.getBody());
        verify(leaveRequestService).countPendingLeaveRequests(1L);
    }

    @Test
    void countThisYearLeave_ShouldReturnCount() {
        when(leaveRequestService.countThisYearLeave(1L, "Approved")).thenReturn(7L);

        ResponseEntity<Long> response = leaveRequestController.countThisYearLeave(1L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(7L, response.getBody());
        verify(leaveRequestService).countThisYearLeave(1L, "Approved");
    }

    @Test
    void getPendingRequests_ShouldReturnPendingList() {
        List<LeaveRequest> pendingRequests = Arrays.asList(sampleLeaveRequest);
        when(leaveRequestService.getPendingRequests()).thenReturn(pendingRequests);

        List<LeaveRequest> response = leaveRequestController.getPendingRequests();

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(pendingRequests, response);
        verify(leaveRequestService).getPendingRequests();
    }

    @Test
    void approveLeave_ShouldCallService() {
        doNothing().when(leaveRequestService).approveLeave(anyLong(), any(LeaveRequest.class));

        ResponseEntity<String> response = leaveRequestController.approveLeave(1L, sampleLeaveRequest);

        verify(leaveRequestService).approveLeave(1L, sampleLeaveRequest);
    }

    @Test
    void rejectLeave_ShouldCallService() {
        doNothing().when(leaveRequestService).rejectLeave(anyLong(), any(LeaveRequest.class));

        ResponseEntity<String> response = leaveRequestController.rejectLeave(1L, sampleLeaveRequest);

        verify(leaveRequestService).rejectLeave(1L, sampleLeaveRequest);
    }

    @Test
    void getLeaveStats_ShouldReturnStats() {
        Map<String, Integer> stats = new HashMap<>();
        stats.put("Annual", 5);
        stats.put("Sick", 3);
        when(leaveRequestService.getLeaveStats(2024, 2)).thenReturn(stats);

        ResponseEntity<Map<String, Integer>> response = leaveRequestController.getLeaveStats(2024, 2);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(stats, response.getBody());
        assertEquals(2, response.getBody().size());
        verify(leaveRequestService).getLeaveStats(2024, 2);
    }

    @Test
    void exportLeaveDataToExcel_Success_ShouldReturnExcelData() throws IOException {
        String base64Excel = "base64EncodedExcelData";
        when(leaveRequestService.exportLeaveDataToExcel(2024, 2)).thenReturn(base64Excel);

        ResponseEntity<String> response = leaveRequestController.exportLeaveDataToExcel(2024, 2);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(base64Excel, response.getBody());
        verify(leaveRequestService).exportLeaveDataToExcel(2024, 2);
    }

    @Test
    void exportLeaveDataToExcel_Error_ShouldReturn500() throws IOException {
        when(leaveRequestService.exportLeaveDataToExcel(2024, 2)).thenThrow(new IOException("Excel generation failed"));

        ResponseEntity<String> response = leaveRequestController.exportLeaveDataToExcel(2024, 2);

        assertNotNull(response);
        assertEquals(500, response.getStatusCodeValue());
        assertEquals("Error generating Excel file", response.getBody());
        verify(leaveRequestService).exportLeaveDataToExcel(2024, 2);
    }

    @Test
    void getLeavesByUserId_ShouldReturnUserLeaves() {
        // Arrange
        List<LeaveRequest> userLeaves = Arrays.asList(sampleLeaveRequest);
        when(leaveRequestService.getLeaveRequestsByUserId(1L)).thenReturn(userLeaves);

        // Act
        List<LeaveRequest> response = leaveRequestController.getLeavesByUserId(1L);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(userLeaves, response);
        verify(leaveRequestService).getLeaveRequestsByUserId(1L);
    }

    @Test
    void getLeaveStats_ByUser_ShouldReturnStats() {
        // Arrange
        Map<String, Integer> expectedStats = new HashMap<>();
        expectedStats.put("ลาป่วย", 2);
        expectedStats.put("ลาพักร้อน", 3);
        expectedStats.put("ลากิจ", 1);
        expectedStats.put("ลาคลอด", 0);
        expectedStats.put("รวม", 6);

        when(leaveRequestService.getApprovedLeaveStats(1L, 2, 2024)).thenReturn(expectedStats);

        // Act
        Map<String, Integer> response = leaveRequestController.getLeaveStats(1L, 2, 2024);

        // Assert
        assertNotNull(response);
        assertEquals(5, response.size());
        assertEquals(expectedStats, response);
        assertEquals(2, response.get("ลาป่วย"));
        assertEquals(3, response.get("ลาพักร้อน"));
        assertEquals(1, response.get("ลากิจ"));
        assertEquals(0, response.get("ลาคลอด"));
        assertEquals(6, response.get("รวม"));
        verify(leaveRequestService).getApprovedLeaveStats(1L, 2, 2024);
    }
}
