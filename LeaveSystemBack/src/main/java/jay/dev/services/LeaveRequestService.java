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
    private LeaveRequestRepository leaveRequestRepository;

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
        return null;
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

    // Get all pending leave requests
    public List<LeaveRequest> getPendingRequests() {
        return leaveRequestRepository.findByStatus("รออนุมัติ");
    }

    // Approve a leave request
    public void approveLeave(Long id, LeaveRequest leaveRequest) {
        LeaveRequest request = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("LeaveRequest with id " + id + " not found"));
        request.setStatus("อนุมัติแล้ว");
        request.setComment(leaveRequest.getComment()); // Store comment
        leaveRequestRepository.save(request);
    }

    // Reject a leave request
    public void rejectLeave(Long id, LeaveRequest leaveRequest) {
        LeaveRequest request = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("LeaveRequest with id " + id + " not found"));
        request.setStatus("ถูกปฏิเสธ");
        request.setComment(leaveRequest.getComment()); // Store comment
        leaveRequestRepository.save(request);
    }
    public long getPendingLeaveCount(Long userId) {
        return leaveRequestRepository.countPendingLeavesByUserId(userId);
    }


    public Map<String, Integer> getLeaveStats(int year, int month) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = YearMonth.of(year, month).atEndOfMonth();

        // Fetch only leave requests where the status is "อนุมัติแล้ว" (Approved)
        List<LeaveRequest> leaves = leaveRequestRepository.findByStartDateBetweenAndStatus(startDate, endDate, "อนุมัติแล้ว");

        Map<String, Integer> stats = new HashMap<>();
        for (LeaveRequest leave : leaves) {
            String leaveType = leave.getLeaveType().getName();
            stats.put(leaveType, stats.getOrDefault(leaveType, 0) + 1);
        }
        return stats;
    }


    public String exportLeaveDataToExcel(int year, int month) throws IOException {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = YearMonth.of(year, month).atEndOfMonth();

        List<LeaveRequest> leaves = leaveRequestRepository.findByStartDateBetween(startDate, endDate);

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

        // Populate rows
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
        return leaveRequestRepository.findByUserId(userId);
    }

}
