import { Component, OnInit } from '@angular/core';
import { LeaveRequestService } from '../../services/leave.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  leaveBalance: number = 0;
  pendingLeaves: number = 0;
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
    this.leaveService.countPendingByUserId(userId).subscribe(
      (pendingCount: number) => this.pendingLeaves = pendingCount,
      error => console.error('Error fetching pending balance:', error)
    );




    // For pending leave requests & used leave this year, you could call appropriate endpoints.
    // Here, we'll simulate those numbers or assume they're part of the leave request list.

    // Get all leave requests (for demonstration purposes)
    this.leaveService.getLeaveRequestsByUserId(userId).subscribe(
      (data) => {
        console.log("API Response:", data);

        this.recentLeaves = Array.isArray(data) ? data : [data]; // Ensure it's an array
        // this.recentLeaves = this.recentLeaves.slice(0, 5); // Keep the last 5 requests

        // Calculate leave days for each request (endDate - startDate + 1)
        this.recentLeaves = this.recentLeaves.map((leave: any) => ({
          ...leave,
          leaveDays: this.calculateLeaveDays(leave.startDate, leave.endDate)
        }));

        const currentYear = new Date().getFullYear();

        // Count only "Approved" leaves that started this year
        this.usedLeaveThisYear = data
          .filter((leave: any) =>
            leave.status === "Approved" &&
            new Date(leave.startDate).getFullYear() === currentYear
          )
          .reduce((sum: number, leave: any) => sum + this.calculateLeaveDays(leave.startDate, leave.endDate), 0);

      },
      error => console.error('Error fetching leave requests:', error)
    );




  }
  calculateLeaveDays(startDate: string, endDate: string): number {
    const start = new Date(startDate);
    const end = new Date(endDate);
    const timeDifference = end.getTime() - start.getTime();
    const days = Math.ceil(timeDifference / (1000 * 60 * 60 * 24)) + 1; // Add 1 to include the last day
    return days;
  }

}
