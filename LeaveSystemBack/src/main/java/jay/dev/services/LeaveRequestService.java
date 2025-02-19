package jay.dev.services;
import jakarta.persistence.EntityNotFoundException;
import jay.dev.entities.LeaveRequest;
import jay.dev.repositories.LeaveRequestRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

@Service
public class LeaveRequestService {

    @Autowired
    private LeaveRequestRepository leaveRequestRepo;

    public enum LeaveStatus {
        PENDING("รออนุมัติ"),
        APPROVED("อนุมัติแล้ว"),
        REJECTED("ถูกปฏิเสธ");

        private final String status;

        LeaveStatus(String status) {
            this.status = status;
        }

        public String getStatus() {
            return status;
        }
    }

    public LeaveRequest createLeaveRequest(LeaveRequest leaveRequest) {
        return leaveRequestRepo.save(leaveRequest);
    }

    public List<LeaveRequest> getAllLeaveRequests() {
        return leaveRequestRepo.findAll();
    }

    public LeaveRequest updateLeaveRequestStatus(Long id, String status) {
        Optional<LeaveRequest> leaveRequestOptional = leaveRequestRepo.findById(id);
        if (leaveRequestOptional.isPresent()) {
            LeaveRequest leaveRequest = leaveRequestOptional.get();
            leaveRequest.setStatus(status);
            return leaveRequestRepo.save(leaveRequest);
        }
        return null;
    }

    public long countPendingLeaveRequests(Long userId) {
        return leaveRequestRepo.countByStatusAndUserId("Pending", userId);
    }

    public long countThisYearLeave(Long userId, String status) {
        return leaveRequestRepo.countByUserIdAndStatusAndStartDateAfter(userId, status, LocalDate.now().withDayOfYear(1));
    }

    // Get all pending leave requests
    public List<LeaveRequest> getPendingRequests() {
        return leaveRequestRepo.findByStatus(LeaveStatus.PENDING.getStatus());
    }

    // Approve a leave request
    public void approveLeave(Long id, LeaveRequest leaveRequest) {
        LeaveRequest request = leaveRequestRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Leave request not found"));
        request.setStatus(LeaveStatus.APPROVED.getStatus());
        request.setComment(leaveRequest.getComment());
        leaveRequestRepo.save(request);
    }

    // Reject a leave request
    public void rejectLeave(Long id, LeaveRequest leaveRequest) {
        LeaveRequest request = leaveRequestRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Leave request not found"));
        request.setStatus(LeaveStatus.REJECTED.getStatus());
        request.setComment(leaveRequest.getComment());
        leaveRequestRepo.save(request);
    }

    public long getPendingLeaveCount(Long userId) {
        return leaveRequestRepo.countPendingLeavesByUserId(userId);
    }

    public Map<String, Integer> getLeaveStats(int year, int month) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = YearMonth.of(year, month).atEndOfMonth();
        List<LeaveRequest> leaves = leaveRequestRepo.findByStartDateBetweenAndStatus(
            startDate, endDate, LeaveStatus.APPROVED.getStatus());
        Map<String, Integer> stats = new HashMap<>();
        for (LeaveRequest leave : leaves) {
            String leaveType = leave.getLeaveType().getName();
            stats.merge(leaveType, 1, Integer::sum);
        }
        return stats;
    }

    public String exportLeaveDataToExcel(int year, int month) throws IOException {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = YearMonth.of(year, month).atEndOfMonth();
        List<LeaveRequest> leaves = leaveRequestRepo.findByStartDateBetween(startDate, endDate);
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Leave Report");

        // Create header row
        Row headerRow = sheet.createRow(0);
        String[] headers = {"Username", "Days", "Reason", "Type", "Branch", "Created At"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(getHeaderCellStyle(workbook));
        }

        int rowIdx = 1;
        for (LeaveRequest leave : leaves) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(leave.getUser().getUsername());
            row.createCell(1).setCellValue(java.time.temporal.ChronoUnit.DAYS.between(leave.getStartDate(), leave.getEndDate()) + 1);
            row.createCell(2).setCellValue(leave.getReason());
            row.createCell(3).setCellValue(leave.getLeaveType().getName());
            row.createCell(4).setCellValue(leave.getUser().getDepartment());
            row.createCell(5).setCellValue(leave.getStartDate().toString());
        }

        // Convert to Base64
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();
        byte[] excelBytes = outputStream.toByteArray();
        return Base64.getEncoder().encodeToString(excelBytes);
    }

    private CellStyle getHeaderCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        return style;
    }

    public List<LeaveRequest> getLeaveRequestsByUserId(Long userId) {
        return leaveRequestRepo.findByUserId(userId);
    }

    public Map<String, Integer> getApprovedLeaveStats(Long userId, int month, int year) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = YearMonth.of(year, month).atEndOfMonth();
        List<LeaveRequest> leaveRequests = leaveRequestRepo.findByUserIdAndStatusAndStartDateBetween(userId, "อนุมัติแล้ว", startDate, endDate);
        // Initialize a map to hold the leave counts for each type
        Map<String, Integer> leaveStats = new HashMap<>();
        leaveStats.put("ลาป่วย", 0);
        leaveStats.put("ลาพักร้อน", 0);
        leaveStats.put("ลากิจ", 0);
        leaveStats.put("ลาคลอด", 0);
        // Loop through the leave requests and count the days for each leave type
        for (LeaveRequest leaveRequest : leaveRequests) {
            String leaveTypeName = leaveRequest.getLeaveType().getName();
            // If leave request matches a known leave type, calculate the leave days
            if (leaveStats.containsKey(leaveTypeName)) {
                // Calculate the number of days the leave request covers
                long leaveDays = java.time.temporal.ChronoUnit.DAYS.between(leaveRequest.getStartDate(), leaveRequest.getEndDate()) + 1;
                leaveStats.put(leaveTypeName, leaveStats.get(leaveTypeName) + (int) leaveDays);
            }
        }

        // Add total leave days
        int totalLeaveDays = leaveStats.values().stream().mapToInt(Integer::intValue).sum();
        leaveStats.put("รวม", totalLeaveDays);
        return leaveStats;
    }
}
