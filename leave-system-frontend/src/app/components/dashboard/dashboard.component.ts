import { Component, OnInit } from '@angular/core';
import { LeaveRequestService } from '../../services/leave.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  leaveBalance: number = 0;
  pendingLeaveRequests: number = 0;
  usedLeaveThisYear: number = 0;
  recentLeaves: any[] = [];

  constructor(private leaveService: LeaveRequestService) {}

  ngOnInit(): void {
    const userId = 1; // Replace with actual user ID as needed

    // Get leave balance for the user
    this.leaveService.getLeaveBalance(userId).subscribe(
      balance => this.leaveBalance = balance,
      error => console.error('Error fetching leave balance:', error)
    );

    // For pending leave requests & used leave this year, you could call appropriate endpoints.
    // Here, we'll simulate those numbers or assume they're part of the leave request list.

    // Get all leave requests (for demonstration purposes)
    this.leaveService.getLeaveRequests().subscribe(
      data => {
        // Filter to recent leaves (e.g., last 5 requests) – adjust logic as needed
        this.recentLeaves = data.slice(0, 5);
        // For pending, count requests with status 'Pending'
        this.pendingLeaveRequests = data.filter((leave: any) => leave.status === 'Pending').length;
        // For used leave this year, sum up days taken for leaves that started this year.
        const currentYear = new Date().getFullYear();
        this.usedLeaveThisYear = data
          .filter((leave: any) => new Date(leave.startDate).getFullYear() === currentYear)
          .reduce((sum: number, leave: any) => sum + leave.daysTaken, 0);
      },
      error => console.error('Error fetching leave requests:', error)
    );
  }
}
