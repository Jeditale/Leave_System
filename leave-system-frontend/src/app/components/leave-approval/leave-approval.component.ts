import { Component } from '@angular/core';

@Component({
  selector: 'app-leave-approval',
  templateUrl: './leave-approval.component.html',
  styleUrls: ['./leave-approval.component.scss']
})
export class LeaveApprovalComponent {
  leaveRequests = [
    { userName: 'John Doe', leaveType: 'Sick', status: 'Pending', id: 1 },
    { userName: 'Jane Smith', leaveType: 'Vacation', status: 'Pending', id: 2 }
  ];

  approveLeave(id: number): void {
    console.log(`Leave request with ID ${id} approved`);
  }

  rejectLeave(id: number): void {
    console.log(`Leave request with ID ${id} rejected`);
  }
}
