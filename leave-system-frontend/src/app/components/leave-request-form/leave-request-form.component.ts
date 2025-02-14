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

  constructor(private readonly leaveService: LeaveRequestService) {}

  submitRequest() {
    const leaveData = {
      user: { id: 1 }, //1 because no login
      leaveType: { id: this.leaveType },
      startDate: this.startDateValue,
      endDate: this.endDateValue,
      status: 'รออนุมัติ',
      reason: this.leaveReason,
      comment: ''
    };

    this.leaveService.createLeaveRequest(leaveData).subscribe(
      response => {


        // Clear input fields after successful submission
        this.leaveType = null;
        this.startDateValue = null;
        this.endDateValue = null;
        this.leaveReason = '';
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
