import { Component } from '@angular/core';
import { MatDatepicker } from '@angular/material/datepicker';
import { Color, ScaleType } from '@swimlane/ngx-charts';

@Component({
  selector: 'app-leave-history',
  templateUrl: './leave-history.component.html',
  styleUrls: ['./leave-history.component.scss']
})
export class LeaveHistoryComponent {
  ngOnInit() {
    this.filterData();
  }

  displayedColumns: string[] = ['userName', 'leaveType', 'daysTaken', 'status'];
  leaveData: any[] = [
    // Example data for leaveData
    { userName: 'John Doe', leaveType: 'Sick', status: 'Approved', department: 'HR', daysTaken: 3 },
    { userName: 'Jane Smith', leaveType: 'Vacation', status: 'Pending', department: 'IT', daysTaken: 2 },
    // Add more data as needed
  ];
  filteredLeaveData = [...this.leaveData];
  totalItems = this.leaveData.length;
  pageSize = 5;

  selectedMonthYear: Date = new Date();
  selectedDepartment: string = 'All';
  selectedLeaveType: string = 'All';
  selectedStatus: string = 'Approved';
  chosenYear: number | null = null;

  departments = ['HR', 'Finance', 'IT', 'Operations', 'All'];
  leaveTypes = ['Sick', 'Vacation', 'Personal', 'Maternity', 'All'];
  statuses = ['Approved', 'Pending', 'Rejected', 'All'];

  // Chart Data
  leaveStatsData = [
    { name: 'Sick Leave', value: 10 },
    { name: 'Vacation Leave', value: 7 },
    { name: 'Personal Leave', value: 4 }
  ];

  colorScheme: Color = {
    domain: ['#FF5733', '#33FF57', '#3357FF'], // Define colors
    group: ScaleType.Ordinal,
    selectable: true,
    name: 'customScheme'
  };

  // Method to set the selected month/year
  setMonthYear(event: Date, datepicker: any) {
    this.selectedMonthYear = event;
    datepicker.close();
  }

  // Method to handle pagination change (if needed)
  onPaginateChange(event: any) {
    console.log('Page Change:', event);
  }

  // Handles year selection
  chosenYearHandler(normalizedYear: Date) {
    this.chosenYear = normalizedYear.getFullYear(); // Store selected year
    console.log(this.chosenYear);
  }

  // Handles month selection
  chosenMonthHandler(normalizedMonth: Date, datepicker: MatDatepicker<Date>) {
    if (this.chosenYear !== null) {
      // Create new Date with chosen year and selected month
      this.selectedMonthYear = new Date(this.chosenYear, normalizedMonth.getMonth());
    } else {
      // Handle case when year is not yet chosen
      this.selectedMonthYear = new Date(normalizedMonth.getFullYear(), normalizedMonth.getMonth());
    }

    datepicker.close(); // Close the picker after selection
    console.log(this.selectedMonthYear); // Output selected month/year
  }

  // Filter data based on selections (Leave Type, Status, Department)
  filterData() {
    this.filteredLeaveData = this.leaveData.filter(leave =>
      (this.selectedLeaveType === 'All' || leave.leaveType === this.selectedLeaveType) &&
      (this.selectedStatus === 'All' || leave.status === this.selectedStatus) &&
      (this.selectedDepartment === 'All' || leave.department === this.selectedDepartment)
    );

    // Update the chart data after filtering
    this.updateChartData();
  }

  // Update chart data based on filtered leave data
  updateChartData() {
  const leaveStats: { [key in 'Sick' | 'Vacation' | 'Personal' | 'Maternity']: number } = {
    'Sick': 0,
    'Vacation': 0,
    'Personal': 0,
    'Maternity': 0
  };

    // Count leave types from filtered data
    this.filteredLeaveData.forEach(leave => {
      if (leaveStats[leave.leaveType as 'Sick' | 'Vacation' | 'Personal' | 'Maternity'] !== undefined) {
        leaveStats[leave.leaveType as 'Sick' | 'Vacation' | 'Personal' | 'Maternity']++;
      }
    });

    // Prepare chart data
    this.leaveStatsData = [
      { name: 'Sick Leave', value: leaveStats['Sick'] },
      { name: 'Vacation Leave', value: leaveStats['Vacation'] },
      { name: 'Personal Leave', value: leaveStats['Personal'] },
      { name: 'Maternity Leave', value: leaveStats['Maternity'] }
    ];
  }


  exportToExcel() {
    // Implement later
  }
}
