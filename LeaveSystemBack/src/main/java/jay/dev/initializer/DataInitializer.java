package jay.dev.initializer;

import jay.dev.entities.LeaveBalance;
import jay.dev.entities.LeaveRequest;
import jay.dev.entities.LeaveType;
import jay.dev.entities.User;
import jay.dev.repositories.LeaveBalanceRepository;
import jay.dev.repositories.LeaveRequestRepository;
import jay.dev.repositories.LeaveTypeRepository;
import jay.dev.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final LeaveTypeRepository leaveTypeRepository;
    private final LeaveRequestRepository leaveRequestRepository;
    private final LeaveBalanceRepository leaveBalanceRepository;

    public DataInitializer(UserRepository userRepository, LeaveTypeRepository leaveTypeRepository, LeaveRequestRepository leaveRequestRepository, LeaveBalanceRepository leaveBalanceRepository) {
        this.userRepository = userRepository;
        this.leaveTypeRepository = leaveTypeRepository;
        this.leaveRequestRepository = leaveRequestRepository;
        this.leaveBalanceRepository = leaveBalanceRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Insert only if the database is empty
        if (userRepository.count() == 0) {
            User user = new User();
            user.setUsername("john.doe");
            user.setEmail("john.doe@example.com");
            user.setRole("Employee");
            user.setDepartment("IT");
            userRepository.save(user);
        }

        if (leaveTypeRepository.count() == 0) {
            List<LeaveType> leaveTypes = List.of(
                    new LeaveType(null, "Sick Leave", "Leave for health issues", 10),
                    new LeaveType(null, "Casual Leave", "Short-term personal leave", 5),
                    new LeaveType(null, "Maternity Leave", "Leave for new mothers", 90),
                    new LeaveType(null, "Paternity Leave", "Leave for new fathers", 15)
            );
            leaveTypeRepository.saveAll(leaveTypes);
        }

        User user = userRepository.findAll().get(0);
        List<LeaveType> leaveTypes = leaveTypeRepository.findAll();

        if (leaveBalanceRepository.count() == 0) {
            for (LeaveType leaveType : leaveTypes) {
                LeaveBalance leaveBalance = new LeaveBalance();
                leaveBalance.setUser(user);
                leaveBalance.setLeaveType(leaveType);
                leaveBalance.setYear(LocalDate.now().getYear());
                leaveBalance.setRemainingDays(leaveType.getMaxDays()); // Set full balance
                leaveBalanceRepository.save(leaveBalance);
            }
        }

        if (leaveRequestRepository.count() == 0) {
            LeaveRequest leaveRequest = new LeaveRequest();
            leaveRequest.setUser(user);
            leaveRequest.setLeaveType(leaveTypes.get(0)); // Assign first leave type
            leaveRequest.setStartDate(LocalDate.now().plusDays(5));
            leaveRequest.setEndDate(LocalDate.now().plusDays(7));
            leaveRequest.setStatus("Pending");
            leaveRequest.setReason("Medical appointment");
            leaveRequestRepository.save(leaveRequest);
        }
    }
}
