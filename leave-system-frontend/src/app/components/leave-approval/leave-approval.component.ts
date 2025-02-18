import { Component, OnInit } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { LeaveRequestService } from '../../services/leave.service';
import { Store } from '@ngrx/store';
import { approveLeaveRequest, rejectLeaveRequest } from '../../store/leave.actions';
@Component({
  selector: 'app-leave-approval',
  templateUrl: './leave-approval.component.html',
  styleUrls: ['./leave-approval.component.scss']
})
export class LeaveApprovalComponent implements OnInit {
  leaveRequests: any[] = [];  // Data from API
  displayedColumns: string[] = ['userName', 'leaveType', 'status'];
  dataSource = new MatTableDataSource<any>([]);
  paginatedData: any[] = [];
  totalItems: number = 0;
  pageSize: number = 5;
  currentPage: number = 0;
  selectedLeaveRequest: any = null;
  comment: string = '';

  constructor(private readonly leaveService: LeaveRequestService,
    private readonly store : Store) {}

  ngOnInit(): void {
    this.loadLeaveRequests();
  }

  loadLeaveRequests() {
    this.leaveService.getPendingLeaveRequests().subscribe(
      (data) => {

        this.leaveRequests = data;
        this.totalItems = this.leaveRequests.length;
        this.paginateData();
      },
      (error) => {
        console.error('Error loading leave requests', error);
      }
    );
  }

  calculateDays(startDate: string, endDate: string): number {
    const start = new Date(startDate);
    const end = new Date(endDate);
    const timeDifference = end.getTime() - start.getTime();
    const days = timeDifference / (1000 * 3600 * 24);
    return days + 1; // Add 1 to include the end date
  }

  onPaginateChange(event: any) {
    this.currentPage = event.pageIndex;
    this.pageSize = event.pageSize;
    this.paginateData();
  }

  paginateData() {
    const startIndex = this.currentPage * this.pageSize;
    const paginatedItems = this.leaveRequests.slice(startIndex, startIndex + this.pageSize);
    this.paginatedData = paginatedItems;
  }

  showDetails(leaveRequest: any) {
    this.selectedLeaveRequest = leaveRequest;
  }

  approveRequest() {
    if (this.selectedLeaveRequest) {
      this.store.dispatch(approveLeaveRequest({requestId:this.selectedLeaveRequest.id,
        comment: this.comment
      }))
          this.selectedLeaveRequest = null;
          this.loadLeaveRequests(); // Reload requests after approval
    }
  }


  rejectRequest() {
    if (this.selectedLeaveRequest) {
      this.store.dispatch(rejectLeaveRequest({requestId:this.selectedLeaveRequest.id,
        comment:this.comment
      }))
          this.selectedLeaveRequest = null;
          this.loadLeaveRequests(); // Reload requests after rejection
    }
  }
}
