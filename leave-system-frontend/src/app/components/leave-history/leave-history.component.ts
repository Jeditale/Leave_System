import { Component } from '@angular/core';

@Component({
  selector: 'app-leave-history',
  templateUrl: './leave-history.component.html',
  styleUrls: ['./leave-history.component.scss']
})
export class LeaveHistoryComponent {
  displayedColumns: string[] = ['userName', 'leaveType', 'status', 'details'];
  leaveHistory = [
    { userName: 'John Doe', leaveType: 'Sick', status: 'Approved' },
    { userName: 'Jane Smith', leaveType: 'Vacation', status: 'Pending' }
  ];
}
