import { Component } from '@angular/core';
import { LeaveRequestService } from '../../services/leave.service';

@Component({
  selector: 'app-leave-request',
  templateUrl: './leave-request-form.component.html',
  styleUrls: ['./leave-request-form.component.scss']
})
export class LeaveRequestFormComponent {
  leaveType: string = '';
  startDateValue: Date | null = null;
  endDateValue: Date | null = null;
  leaveReason: string = '';

  constructor(private leaveService: LeaveRequestService) {}

  submitRequest() {
    const leaveData = {
      // Fill in your user details as required
      user: { id: 1 }, // Replace with actual user ID
      leaveType: { id: 1, name: this.leaveType },
      startDate: this.startDateValue,
      endDate: this.endDateValue,
      status: 'Pending',
      reason: this.leaveReason,
      comment: ''
    };

    this.leaveService.getLeaveRequests(leaveData).subscribe(response => {
      console.log('Leave request submitted', response);
    });
  }

  clearForm(form: any) {
    form.resetForm();
  }
}
