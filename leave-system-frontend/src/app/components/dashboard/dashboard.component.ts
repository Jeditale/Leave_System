import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  leaveBalance: number = 15;  // Example data
  pendingLeaveRequests: number = 3;  // Example data
  usedLeaveThisYear: number = 5;  // Example data
  recentLeaves = [
    { userName: 'John Doe', leaveType: 'Sick',leaveDays:12 ,status: 'Pending' },
    { userName: 'Jane Smith', leaveType: 'Vacation',leaveDays:11, status: 'Approved' }
  ];

  ngOnInit(): void {}
}

