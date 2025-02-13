import { Component, OnInit } from '@angular/core';
import { LeaveRequestService } from '../../services/leave.service';
import { Color, ScaleType } from '@swimlane/ngx-charts';

@Component({
  selector: 'app-leave-history',
  templateUrl: './leave-history.component.html',
  styleUrls: ['./leave-history.component.scss']
})
export class LeaveHistoryComponent implements OnInit {
  // Table configuration
  displayedColumns: string[] = ['userName', 'leaveType', 'daysTaken', 'status'];
  leaveData: any[] = [];
  filteredLeaveData: any[] = [];
  totalItems: number = 0;
  pageSize: number = 5;

  // Filters
  selectedMonthYear: Date = new Date();
  selectedDepartment: string = 'All';
  selectedLeaveType: string = 'All';
  selectedStatus: string = 'All';
  chosenYear: number | null = null;
  departments = ['HR', 'Finance', 'IT', 'Operations', 'All'];
  leaveTypes = ['Sick', 'Vacation', 'Personal', 'Maternity', 'All'];
  statuses = ['Approved', 'Pending', 'Rejected', 'All'];

  // Chart Data
  leaveStatsData: any[] = [];
  colorScheme: Color = {
    domain: ['#FF5733', '#33FF57', '#3357FF'], // Define colors
    group: ScaleType.Ordinal,
    selectable: true,
    name: 'customScheme'
  };

  // Selected leave request for details view
  selectedLeaveRequest: any = null;

  constructor(private leaveService: LeaveRequestService) {}

  ngOnInit(): void {
    this.fetchLeaveHistory();
    this.fetchLeaveStats();
  }

  // Fetch all leave history (from backend)
  fetchLeaveHistory() {
    this.leaveService.getLeaveRequests().subscribe(data => {
      this.leaveData = data;
      this.filteredLeaveData = [...this.leaveData];
      this.totalItems = this.leaveData.length;
    });
  }

  // Fetch leave statistics for the selected month/year
  fetchLeaveStats() {
    const year = this.selectedMonthYear.getFullYear();
    const month = this.selectedMonthYear.getMonth() + 1; // API expects 1-indexed month
    this.leaveService.getLeaveStats(year, month).subscribe(stats => {
      this.leaveStatsData = stats;
    });
  }

  // For month/year picker
  chosenYearHandler(normalizedYear: Date) {
    this.chosenYear = normalizedYear.getFullYear();
  }

  chosenMonthHandler(normalizedMonth: Date, datepicker: any) {
    if (this.chosenYear !== null) {
      this.selectedMonthYear = new Date(this.chosenYear, normalizedMonth.getMonth());
    } else {
      this.selectedMonthYear = new Date(normalizedMonth.getFullYear(), normalizedMonth.getMonth());
    }
    datepicker.close();
    this.fetchLeaveStats(); // Refresh chart when month/year changes
  }

  // Filter table data based on selected filters
  filterData() {
    this.filteredLeaveData = this.leaveData.filter(leave =>
      (this.selectedLeaveType === 'All' || leave.leaveType === this.selectedLeaveType) &&
      (this.selectedStatus === 'All' || leave.status === this.selectedStatus) &&
      (this.selectedDepartment === 'All' || leave.department === this.selectedDepartment)
    );
    this.totalItems = this.filteredLeaveData.length;
  }

  // Handle pagination (if needed)
  onPaginateChange(event: any) {
    // For example, if you want to handle pagination manually
    console.log('Page Change:', event);
  }
  exportToExcel() {
    const year = this.selectedMonthYear.getFullYear();
    const month = this.selectedMonthYear.getMonth() + 1;
    this.leaveService.exportLeaveData(year, month).subscribe(base64 => {
      const link = document.createElement('a');
      link.href = `data:application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;base64,${base64}`;
      link.download = `Leave_History_${year}_${month}.xlsx`;
      link.click();
    });
  }
}
