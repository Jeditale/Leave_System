package jay.dev.services;

import jakarta.persistence.EntityNotFoundException;
import jay.dev.entities.LeaveRequest;
import jay.dev.entities.LeaveType;
import jay.dev.entities.User;
import jay.dev.repositories.LeaveRequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class LeaveRequestServiceTest {

    @Mock
    private LeaveRequestRepository leaveRequestRepository;

    @InjectMocks
    private LeaveRequestService leaveRequestService;

    private LeaveRequest sampleLeaveRequest;
    private LocalDate startDate;
    private LocalDate endDate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // Setup test request data
        sampleLeaveRequest = new LeaveRequest();
        sampleLeaveRequest.setId(1L);
        sampleLeaveRequest.setStatus("Pending");
        
        LeaveType leaveType = new LeaveType();
        leaveType.setName("Annual Leave");
        sampleLeaveRequest.setLeaveType(leaveType);

        startDate = LocalDate.now();
        endDate = startDate.plusDays(5);
        sampleLeaveRequest.setStartDate(startDate);
        sampleLeaveRequest.setEndDate(endDate);
    }

    @Test
    void shouldCreateAndSaveLeaveRequest() {
        when(leaveRequestRepository.save(any(LeaveRequest.class))).thenReturn(sampleLeaveRequest);
        LeaveRequest result = leaveRequestService.createLeaveRequest(sampleLeaveRequest);
        assertNotNull(result);
        assertEquals(sampleLeaveRequest.getId(), result.getId());
        verify(leaveRequestRepository).save(sampleLeaveRequest);
    }

    @Test
    void shouldReturnAllLeaveRequests() {
        List<LeaveRequest> expectedRequests = Arrays.asList(sampleLeaveRequest, new LeaveRequest());
        when(leaveRequestRepository.findAll()).thenReturn(expectedRequests);
        List<LeaveRequest> result = leaveRequestService.getAllLeaveRequests();
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(leaveRequestRepository).findAll();
    }

    @Test
    void shouldUpdateStatusWhenRequestExists() {
        when(leaveRequestRepository.findById(1L)).thenReturn(Optional.of(sampleLeaveRequest));
        when(leaveRequestRepository.save(any(LeaveRequest.class))).thenReturn(sampleLeaveRequest);
        LeaveRequest result = leaveRequestService.updateLeaveRequestStatus(1L, "Approved");
        assertNotNull(result);
        assertEquals("Approved", result.getStatus());
        verify(leaveRequestRepository).save(result);
    }

    @Test
    void shouldReturnNullWhenRequestNotFound() {
        when(leaveRequestRepository.findById(1L)).thenReturn(Optional.empty());
        LeaveRequest result = leaveRequestService.updateLeaveRequestStatus(1L, "Approved");
        assertNull(result);
        verify(leaveRequestRepository, never()).save(any());
    }

    @Test
    void shouldReturnPendingRequestCount() {
        when(leaveRequestRepository.countByStatusAndUserId("Pending", 1L)).thenReturn(5L);
        long result = leaveRequestService.countPendingLeaveRequests(1L);
        assertEquals(5L, result);
        verify(leaveRequestRepository).countByStatusAndUserId("Pending", 1L);
    }

    @Test
    void shouldReturnCurrentYearLeaveCount() {
        when(leaveRequestRepository.countByUserIdAndStatusAndStartDateAfter(
            eq(1L), eq("Approved"), any(LocalDate.class))).thenReturn(3L);
        long result = leaveRequestService.countThisYearLeave(1L, "Approved");
        assertEquals(3L, result);
        verify(leaveRequestRepository).countByUserIdAndStatusAndStartDateAfter(
            eq(1L), eq("Approved"), any(LocalDate.class));
    }

    @Test
    void shouldReturnListOfPendingRequests() {
        List<LeaveRequest> pendingRequests = Arrays.asList(sampleLeaveRequest);
        when(leaveRequestRepository.findByStatus("รออนุมัติ")).thenReturn(pendingRequests);
        List<LeaveRequest> result = leaveRequestService.getPendingRequests();
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(leaveRequestRepository).findByStatus("รออนุมัติ");
    }

    @Test
    void shouldApproveExistingLeaveRequest() {
        when(leaveRequestRepository.findById(1L)).thenReturn(Optional.of(sampleLeaveRequest));
        when(leaveRequestRepository.save(any(LeaveRequest.class))).thenReturn(sampleLeaveRequest);
        LeaveRequest updateRequest = new LeaveRequest();
        updateRequest.setComment("Approved with conditions");
        leaveRequestService.approveLeave(1L, updateRequest);
        assertEquals("อนุมัติแล้ว", sampleLeaveRequest.getStatus());
        assertEquals("Approved with conditions", sampleLeaveRequest.getComment());
        verify(leaveRequestRepository).save(sampleLeaveRequest);
    }

    @Test
    void shouldThrowExceptionWhenApprovingNonExistingLeaveRequest() {
        when(leaveRequestRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> 
            leaveRequestService.approveLeave(1L, new LeaveRequest())
        );
        verify(leaveRequestRepository, never()).save(any());
    }

    @Test
    void shouldRejectExistingLeaveRequest() {
        when(leaveRequestRepository.findById(1L)).thenReturn(Optional.of(sampleLeaveRequest));
        when(leaveRequestRepository.save(any(LeaveRequest.class))).thenReturn(sampleLeaveRequest);
        LeaveRequest updateRequest = new LeaveRequest();
        updateRequest.setComment("Rejected due to workload");
        leaveRequestService.rejectLeave(1L, updateRequest);
        assertEquals("ถูกปฏิเสธ", sampleLeaveRequest.getStatus());
        assertEquals("Rejected due to workload", sampleLeaveRequest.getComment());
        verify(leaveRequestRepository).save(sampleLeaveRequest);
    }

    @Test
    void shouldThrowExceptionWhenRejectingNonExistingLeaveRequest() {
        when(leaveRequestRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> 
            leaveRequestService.rejectLeave(1L, new LeaveRequest())
        );
        verify(leaveRequestRepository, never()).save(any());
    }

    @Test
    void shouldReturnPendingLeaveCount() {
        when(leaveRequestRepository.countPendingLeavesByUserId(1L)).thenReturn(3L);
        long result = leaveRequestService.getPendingLeaveCount(1L);
        assertEquals(3L, result);
        verify(leaveRequestRepository).countPendingLeavesByUserId(1L);
    }

    @Test
    void shouldReturnLeaveStats() {
        LeaveRequest request1 = new LeaveRequest();
        LeaveType type1 = new LeaveType();
        type1.setName("Annual");
        request1.setLeaveType(type1);

        LeaveRequest request2 = new LeaveRequest();
        LeaveType type2 = new LeaveType();
        type2.setName("Sick");
        request2.setLeaveType(type2);

        List<LeaveRequest> requests = Arrays.asList(request1, request2);
        when(leaveRequestRepository.findByStartDateBetweenAndStatus(
            any(LocalDate.class), any(LocalDate.class), eq("อนุมัติแล้ว")))
            .thenReturn(requests);

        Map<String, Integer> stats = leaveRequestService.getLeaveStats(2024, 2);

        assertNotNull(stats);
        assertEquals(2, stats.size());
        assertEquals(1, stats.get("Annual"));
        assertEquals(1, stats.get("Sick"));
        verify(leaveRequestRepository).findByStartDateBetweenAndStatus(
            any(LocalDate.class), any(LocalDate.class), eq("อนุมัติแล้ว"));
    }

    @Test
    void shouldExportLeaveDataToExcel() throws IOException {
        // Create a Test user
        User user = new User();
        user.setId(1L);
        user.setUsername("testUser");
        user.setDepartment("IT");
        // Create a complete leave type
        LeaveType leaveType = new LeaveType();
        leaveType.setId(1L);
        leaveType.setName("Annual Leave");
        // Create a complete leave request
        LeaveRequest request = new LeaveRequest();
        request.setId(1L);
        request.setUser(user);
        request.setLeaveType(leaveType);
        request.setStartDate(LocalDate.now());
        request.setEndDate(LocalDate.now().plusDays(2));
        request.setReason("Vacation");

        List<LeaveRequest> requests = Arrays.asList(request);
        when(leaveRequestRepository.findByStartDateBetween(
            any(LocalDate.class), any(LocalDate.class)))
            .thenReturn(requests);

        String result = leaveRequestService.exportLeaveDataToExcel(2024, 2);

        assertNotNull(result);
        assertTrue(result.length() > 0);
        verify(leaveRequestRepository).findByStartDateBetween(
            any(LocalDate.class), any(LocalDate.class));
    }

    @Test
    void shouldReturnLeaveRequestsByUserId() {
        List<LeaveRequest> expectedLeaves = Arrays.asList(sampleLeaveRequest);
        when(leaveRequestRepository.findByUserId(1L)).thenReturn(expectedLeaves);
        List<LeaveRequest> result = leaveRequestService.getLeaveRequestsByUserId(1L);
        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(expectedLeaves, result);
        verify(leaveRequestRepository).findByUserId(1L);
    }

    @Test
    void shouldCalculateApprovedLeaveStats() {
        User user = new User();
        user.setId(1L);
        // Create leave types
        LeaveType sickLeave = new LeaveType();
        sickLeave.setName("ลาป่วย");
        
        LeaveType annualLeave = new LeaveType();
        annualLeave.setName("ลาพักร้อน");
        // Create leave requests
        LeaveRequest request1 = new LeaveRequest();
        request1.setUser(user);
        request1.setLeaveType(sickLeave);
        request1.setStartDate(LocalDate.of(2024, 2, 1));
        request1.setEndDate(LocalDate.of(2024, 2, 2)); // 2 days

        LeaveRequest request2 = new LeaveRequest();
        request2.setUser(user);
        request2.setLeaveType(annualLeave);
        request2.setStartDate(LocalDate.of(2024, 2, 15));
        request2.setEndDate(LocalDate.of(2024, 2, 17)); // 3 days

        List<LeaveRequest> leaveRequests = Arrays.asList(request1, request2);

        when(leaveRequestRepository.findByUserIdAndStatusAndStartDateBetween(
            eq(1L), 
            eq("อนุมัติแล้ว"), 
            any(LocalDate.class), 
            any(LocalDate.class)
        )).thenReturn(leaveRequests);


        Map<String, Integer> result = leaveRequestService.getApprovedLeaveStats(1L, 2, 2024);


        assertNotNull(result);
        assertEquals(5, result.size());
        assertEquals(2, result.get("ลาป่วย"));
        assertEquals(3, result.get("ลาพักร้อน"));
        assertEquals(0, result.get("ลากิจ"));
        assertEquals(0, result.get("ลาคลอด"));
        assertEquals(5, result.get("รวม"));

        verify(leaveRequestRepository).findByUserIdAndStatusAndStartDateBetween(
            eq(1L), 
            eq("อนุมัติแล้ว"), 
            eq(LocalDate.of(2024, 2, 1)),
            eq(LocalDate.of(2024, 2, 29))
        );
    }

    @Test
    void shouldReturnZeroApprovedLeaveStatsWhenNoLeaves() {
        when(leaveRequestRepository.findByUserIdAndStatusAndStartDateBetween(
            anyLong(), 
            anyString(), 
            any(LocalDate.class), 
            any(LocalDate.class)
        )).thenReturn(Collections.emptyList());
        Map<String, Integer> result = leaveRequestService.getApprovedLeaveStats(1L, 2, 2024);

        assertNotNull(result);
        assertEquals(5, result.size());
        assertEquals(0, result.get("ลาป่วย"));
        assertEquals(0, result.get("ลาพักร้อน"));
        assertEquals(0, result.get("ลากิจ"));
        assertEquals(0, result.get("ลาคลอด"));
        assertEquals(0, result.get("รวม"));

        verify(leaveRequestRepository).findByUserIdAndStatusAndStartDateBetween(
            eq(1L), 
            eq("อนุมัติแล้ว"), 
            any(LocalDate.class),
            any(LocalDate.class)
        );
    }

    @Test
    void shouldIgnoreUnknownLeaveTypeWhenCalculatingApprovedLeaveStats() {
        User user = new User();
        user.setId(1L);

        LeaveType sickLeave = new LeaveType();
        sickLeave.setName("ลาป่วย");
        
        LeaveType unknownLeave = new LeaveType();
        unknownLeave.setName("ลาประเภทอื่นๆ");

        LeaveRequest request1 = new LeaveRequest();
        request1.setUser(user);
        request1.setLeaveType(sickLeave);
        request1.setStartDate(LocalDate.of(2024, 2, 1));
        request1.setEndDate(LocalDate.of(2024, 2, 2)); // 2 days

        LeaveRequest request2 = new LeaveRequest();
        request2.setUser(user);
        request2.setLeaveType(unknownLeave);
        request2.setStartDate(LocalDate.of(2024, 2, 15));
        request2.setEndDate(LocalDate.of(2024, 2, 17)); // 3 days (should be ignored)

        List<LeaveRequest> leaveRequests = Arrays.asList(request1, request2);

        when(leaveRequestRepository.findByUserIdAndStatusAndStartDateBetween(
            eq(1L), 
            eq("อนุมัติแล้ว"), 
            any(LocalDate.class), 
            any(LocalDate.class)
        )).thenReturn(leaveRequests);

        Map<String, Integer> result = leaveRequestService.getApprovedLeaveStats(1L, 2, 2024);

        assertNotNull(result);
        assertEquals(5, result.size());
        assertEquals(2, result.get("ลาป่วย"));
        assertEquals(0, result.get("ลาพักร้อน"));
        assertEquals(0, result.get("ลากิจ"));
        assertEquals(0, result.get("ลาคลอด"));
        // Total should only include the known leave type (2 days)
        assertEquals(2, result.get("รวม"));
        // Unknown leave type should not be in the map
        assertFalse(result.containsKey("ลาประเภทอื่นๆ"));

        verify(leaveRequestRepository).findByUserIdAndStatusAndStartDateBetween(
            eq(1L), 
            eq("อนุมัติแล้ว"), 
            eq(LocalDate.of(2024, 2, 1)),
            eq(LocalDate.of(2024, 2, 29))
        );
    }

    @Test
    void approveLeaveRequestWithValidComment() {
        String approvalComment = "Approved - Team capacity is sufficient";
        when(leaveRequestRepository.findById(1L)).thenReturn(Optional.of(sampleLeaveRequest));
        when(leaveRequestRepository.save(any(LeaveRequest.class))).thenReturn(sampleLeaveRequest);

        LeaveRequest updateRequest = new LeaveRequest();
        updateRequest.setComment(approvalComment);
        
        leaveRequestService.approveLeave(1L, updateRequest);
        // Verify
        assertEquals("อนุมัติแล้ว", sampleLeaveRequest.getStatus());
        assertEquals(approvalComment, sampleLeaveRequest.getComment());
        verify(leaveRequestRepository).save(sampleLeaveRequest);
    }

    @Test
    void rejectLeaveRequestWithValidReason() {
        String rejectionReason = "Team capacity is limited during this period";
        when(leaveRequestRepository.findById(1L)).thenReturn(Optional.of(sampleLeaveRequest));
        when(leaveRequestRepository.save(any(LeaveRequest.class))).thenReturn(sampleLeaveRequest);

        LeaveRequest updateRequest = new LeaveRequest();
        updateRequest.setComment(rejectionReason);

        leaveRequestService.rejectLeave(1L, updateRequest);
        // Verify
        assertEquals("ถูกปฏิเสธ", sampleLeaveRequest.getStatus());
        assertEquals(rejectionReason, sampleLeaveRequest.getComment());
        verify(leaveRequestRepository).save(sampleLeaveRequest);
    }

    @Test
    void calculateMonthlyLeaveStatistics() {
        LeaveRequest annualLeave = createLeaveRequest("Annual Leave", "อนุมัติแล้ว");
        LeaveRequest sickLeave = createLeaveRequest("Sick Leave", "อนุมัติแล้ว");
        LocalDate startDate = LocalDate.of(2024, 2, 1);
        LocalDate endDate = YearMonth.of(2024, 2).atEndOfMonth();
        when(leaveRequestRepository.findByStartDateBetweenAndStatus(startDate, endDate, "อนุมัติแล้ว"))
            .thenReturn(Arrays.asList(annualLeave, sickLeave));

        Map<String, Integer> stats = leaveRequestService.getLeaveStats(2024, 2);
        assertEquals(1, stats.get("Annual Leave"));
        assertEquals(1, stats.get("Sick Leave"));
    }

    private LeaveRequest createLeaveRequest(String type, String status) {
        LeaveRequest request = new LeaveRequest();
        LeaveType leaveType = new LeaveType();
        leaveType.setName(type);
        request.setLeaveType(leaveType);
        request.setStatus(status);
        return request;
    }
}