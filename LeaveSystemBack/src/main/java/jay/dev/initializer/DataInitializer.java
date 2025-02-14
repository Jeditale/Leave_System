package jay.dev.initializer;

import jakarta.annotation.PostConstruct;
import jay.dev.entities.LeaveBalance;
import jay.dev.entities.LeaveType;
import jay.dev.entities.User;
import jay.dev.repositories.LeaveBalanceRepository;
import jay.dev.repositories.LeaveTypeRepository;
import jay.dev.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class DataInitializer {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LeaveTypeRepository leaveTypeRepository;

    @Autowired
    private LeaveBalanceRepository leaveBalanceRepository;

    @PostConstruct
    public void initializeData() {
        // Check if data is already populated, to ensure this runs only once
        if (userRepository.count() == 0) {
            initializeUsers();
        }
        if (leaveTypeRepository.count() == 0) {
            initializeLeaveTypes();
        }
        if (leaveBalanceRepository.count() == 0) {
            initializeLeaveBalances();
        }
    }

    private void initializeUsers() {
        User user = new User();
        user.setUsername("Jd123");
        user.setEmail("Jeditale@hotmail.com");
        user.setRole("Back-end");
        user.setDepartment("IT");

        userRepository.save(user);
    }

    private void initializeLeaveTypes() {
        LeaveType leaveType1 = new LeaveType(5L, "ลาป่วย", "Sick Leave", 10);
        LeaveType leaveType2 = new LeaveType(6L, "ลาพักร้อน", "Vacation Leave", 5);
        LeaveType leaveType3 = new LeaveType(7L, "ลากิจ", "Personal Leave", 90);
        LeaveType leaveType4 = new LeaveType(8L, "ลาคลอด", "Maternity Leave", 15);

        leaveTypeRepository.save(leaveType1);
        leaveTypeRepository.save(leaveType2);
        leaveTypeRepository.save(leaveType3);
        leaveTypeRepository.save(leaveType4);
    }

    private void initializeLeaveBalances() {
        User user = userRepository.findByUsername("Jd123"); // Find the user by username
        LeaveType leaveType = leaveTypeRepository.findById(5L).orElse(null); // Get the leave type for sick leave

        if (user != null && leaveType != null) {
            LeaveBalance leaveBalance = new LeaveBalance();
            leaveBalance.setUser(user);
            leaveBalance.setLeaveType(leaveType);
            leaveBalance.setYear(2025);
            leaveBalance.setRemainingDays(10);

            leaveBalanceRepository.save(leaveBalance);
        }
    }
}
