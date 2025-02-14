import { Component } from '@angular/core';
import { LeaveRequestService } from '../../services/leave.service';

@Component({
  selector: 'app-leave-request',
  templateUrl: './leave-request-form.component.html',
  styleUrls: ['./leave-request-form.component.scss']
})
export class LeaveRequestFormComponent {
  leaveType: number|null = null;
  startDateValue: Date | null = null;
  endDateValue: Date | null = null;
  leaveReason: string = '';

  constructor(private leaveService: LeaveRequestService) {}

  submitRequest() {
    const leaveData = {
      user: { id: 1 }, // Replace with actual user ID
      leaveType: { id: this.leaveType }, // Only send ID
      startDate: this.startDateValue,
      endDate: this.endDateValue,
      status: 'รออนุมัติ',
      reason: this.leaveReason,
      comment: ''
    };

    this.leaveService.createLeaveRequest(leaveData).subscribe(
      response => {
        console.log('Leave request submitted', response);

        // Clear input fields after successful submission
        this.leaveType = null; // Clear leaveType input
        this.startDateValue = null; // Clear start date input
        this.endDateValue = null; // Clear end date input
        this.leaveReason = ''; // Clear leave reason input
      },
      error => {
        console.error('Error submitting leave request:', error);
      }
    );
  }


  clearForm(form: any) {
    form.resetForm();
  }
}
