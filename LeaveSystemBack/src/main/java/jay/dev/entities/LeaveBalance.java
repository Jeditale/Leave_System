package jay.dev.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "leave_balances")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeaveBalance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private LeaveType leaveType;

    private Integer year;
    private Integer remainingDays;
}
