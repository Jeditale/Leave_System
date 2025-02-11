import { Component, OnInit } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';

@Component({
  selector: 'app-leave-approval',
  templateUrl: './leave-approval.component.html',
  styleUrls: ['./leave-approval.component.scss']
})
export class LeaveApprovalComponent implements OnInit {
    // Simulating a list of leave requests
    leaveRequests = [
      { userName: 'John Doe', leaveType: 'Vacation', status: 'Pending', startDate: '2025-03-01', endDate: '2025-03-05', leaveReason: 'Family trip' },
      { userName: 'Jane Smith', leaveType: 'Sick', status: 'Pending', startDate: '2025-02-20', endDate: '2025-02-22', leaveReason: 'Health issues' },
      // More sample data...
    ];
  displayedColumns: string[] = ['userName', 'leaveType', 'status'];
  dataSource = new MatTableDataSource<any>([]);
  paginatedData: any[] = [];
  totalItems: number = this.leaveRequests.length // Example, set based on your backend response
  pageSize: number = 5;  // Items per page
  currentPage: number = 0;
  selectedLeaveRequest: any = null;  // Store the selected leave request
  comment: string = '';

// In your component class

calculateDays(startDate: string, endDate: string): number {
  const start = new Date(startDate);
  const end = new Date(endDate);
  const timeDifference = end.getTime() - start.getTime();
  const days = timeDifference / (1000 * 3600 * 24); // Convert milliseconds to days
  return days + 1; // Add 1 to include the end date
}


  constructor() {}

  ngOnInit(): void {
    this.paginateData();
  }

  // Pagination handler
  onPaginateChange(event: any) {
    this.currentPage = event.pageIndex;
    this.pageSize = event.pageSize;
    this.paginateData();
  }

  // Pagination logic
  paginateData() {
    const startIndex = this.currentPage * this.pageSize;
    const paginatedItems = this.leaveRequests.slice(startIndex, startIndex + this.pageSize);
    this.paginatedData = paginatedItems;
  }

  // Show details for selected leave request
  showDetails(leaveRequest: any) {
    this.selectedLeaveRequest = leaveRequest;
  }

  // Approve the leave request
  approveRequest() {
    console.log('Leave request approved');
    this.selectedLeaveRequest = null;  // Hide the details after approval
  }

  // Reject the leave request
  rejectRequest() {
    console.log('Leave request rejected');
    this.selectedLeaveRequest = null;  // Hide the details after rejection
  }
}
