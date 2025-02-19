package jay.dev.services;

import jay.dev.entities.LeaveBalance;
import jay.dev.entities.User;
import jay.dev.repositories.LeaveBalanceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

class LeaveBalanceServiceTest {

    @Mock
    private LeaveBalanceRepository leaveBalanceRepository;

    @InjectMocks
    private LeaveBalanceService leaveBalanceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnBalanceWhenUserExists() {
        User user = new User();
        user.setId(1L);
        LeaveBalance expectedBalance = new LeaveBalance();
        expectedBalance.setUser(user);
        expectedBalance.setRemainingDays(10);
        when(leaveBalanceRepository.findByUserId(1L)).thenReturn(Collections.singletonList(expectedBalance));
        LeaveBalance actualBalance = leaveBalanceService.getLeaveBalanceByUserId(1L);

        assertNotNull(actualBalance);
        assertEquals(1L, actualBalance.getUser().getId());
        assertEquals(expectedBalance.getRemainingDays(), actualBalance.getRemainingDays());
    }

    @Test
    void shouldThrowExceptionWhenBalanceNotFound() {
        when(leaveBalanceRepository.findByUserId(anyLong())).thenReturn(Collections.emptyList());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> 
            leaveBalanceService.getLeaveBalanceByUserId(1L)
        );
        assertEquals("Leave balance for user with ID 1 not found", exception.getMessage());
    }
}