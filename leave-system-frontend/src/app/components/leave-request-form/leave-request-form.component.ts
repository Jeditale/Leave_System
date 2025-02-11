import { Component } from '@angular/core';

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

  clearForm(form: any) {
    form.resetForm();
  }

}
