import { Component, OnInit } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { Store } from '@ngrx/store';
import { loadLeaveRequests, approveLeaveRequest, rejectLeaveRequest } from '../../store/leave.actions';
import { selectLeaveRequests } from '../../store/leave.selectors';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-leave-approval',
  templateUrl: './leave-approval.component.html',
  styleUrls: ['./leave-approval.component.scss']
})
export class LeaveApprovalComponent implements OnInit {
  displayedColumns: string[] = ['userName', 'leaveType', 'status'];
  dataSource = new MatTableDataSource<any>([]);
  paginatedData: any[] = [];
  totalItems: number = 0;
  pageSize: number = 5;
  currentPage: number = 0;
  selectedLeaveRequest: any = null;
  comment: string = '';

  leaveRequests$: Observable<any[]>;

  constructor(private store: Store) {
    this.leaveRequests$ = this.store.select(selectLeaveRequests);
  }

  ngOnInit(): void {
    this.store.dispatch(loadLeaveRequests());
    this.leaveRequests$.subscribe(leaveRequests => {
      this.dataSource.data = leaveRequests;
      this.totalItems = leaveRequests.length;
      this.paginateData();
    });
  }

  onPaginateChange(event: any) {
    this.currentPage = event.pageIndex;
    this.pageSize = event.pageSize;
    this.paginateData();
  }

  paginateData() {
    const startIndex = this.currentPage * this.pageSize;
    this.paginatedData = this.dataSource.data.slice(startIndex, startIndex + this.pageSize);
  }

  showDetails(leaveRequest: any) {
    this.selectedLeaveRequest = leaveRequest;
  }

  approveRequest() {
    if (this.selectedLeaveRequest) {
      this.store.dispatch(approveLeaveRequest({ requestId: this.selectedLeaveRequest.id }));
      this.selectedLeaveRequest = null;
    }
  }

  rejectRequest() {
    if (this.selectedLeaveRequest) {
      this.store.dispatch(rejectLeaveRequest({ requestId: this.selectedLeaveRequest.id, comment: this.comment }));
      this.selectedLeaveRequest = null;
      this.comment = '';
    }
  }
}
