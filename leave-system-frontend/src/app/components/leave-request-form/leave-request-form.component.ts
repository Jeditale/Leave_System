import { Component } from '@angular/core';
import { Store } from '@ngrx/store';
import { createLeaveRequest } from '../../store/leave.actions';

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

  constructor(private store: Store) {}

  submitRequest() {
    if (this.leaveType === null) {
      console.error('Leave type is required');
      return;
    }
    const leaveData = {
      user: { id: 1 }, //1 because no login
      leaveType: { id: this.leaveType },
      startDate: this.startDateValue ? this.startDateValue.toISOString() : '',
      endDate: this.endDateValue ? this.endDateValue.toISOString() : '',
      status: 'รออนุมัติ',
      reason: this.leaveReason,
      // comment: ''
    };


    this.store.dispatch(createLeaveRequest({createRequest:leaveData}))
        // Clear input fields after successful submission
        this.leaveType = null;
        this.startDateValue = null;
        this.endDateValue = null;
        this.leaveReason = '';

  }


  clearForm(form: any) {
    form.resetForm();
  }
}
