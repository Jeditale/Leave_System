import { LeaveRequestService } from './../../services/leave.service';
import { Component, OnInit } from '@angular/core';
import { MatDatepicker } from '@angular/material/datepicker';
import { Color, ScaleType } from '@swimlane/ngx-charts';

import { Observable } from 'rxjs';

@Component({
  selector: 'app-leave-history',
  templateUrl: './leave-history.component.html',
  styleUrls: ['./leave-history.component.scss']
})
export class LeaveHistoryComponent implements OnInit {
  ngOnInit() {
    this.filterData();  // Fetch initial data on load
  }

  displayedColumns: string[] = ['userName', 'leaveType', 'daysTaken', 'status'];
  leaveData: any[] = [];
  filteredLeaveData: any[] = [];
  totalItems = 0;
  pageSize = 5;

  selectedMonthYear: Date = new Date();
  selectedDepartment: string = 'ทั้งหมด';
  selectedLeaveType: string = 'ทั้งหมด';
  selectedStatus: string = 'อนุมัติแล้ว';
  chosenYear: number | null = null;


  departments = ['HR', 'การเงิน', 'IT', 'ทั้งหมด'];
  leaveTypes = ['ลาป่วย', 'ลาพักร้อน', 'ลากิจ', 'ลาคลอด', 'ทั้งหมด'];
  statuses = ['อนุมัติแล้ว', 'รอดำเนินการ', 'ถูกปฏิเสธ', 'ทั้งหมด'];
  // Chart Data
  leaveStatsData = [
    { name: 'ลาป่วย', value: 0 },
    { name: 'ลาพักร้อน', value: 0 },
    { name: 'ลากิจ', value: 0 },
    { name: 'Maternity Leave', value: 0 }
  ];

  colorScheme: Color = {
    domain: ['#FF5733', '#33FF57', '#3357FF'], // Define colors
    group: ScaleType.Ordinal,
    selectable: true,
    name: 'customScheme'
  };

  constructor(private leaveService: LeaveRequestService) {}

  // Method to set the selected month/year
  setMonthYear(event: Date, datepicker: any) {
    this.selectedMonthYear = event;
    datepicker.close();
    this.filterData();  // Update data when month/year is selected
  }

  // Method to handle pagination change (if needed)
  onPaginateChange(event: any) {
    console.log('Page Change:', event);
  }

  // Handles year selection
  chosenYearHandler(normalizedYear: Date) {
    this.chosenYear = normalizedYear.getFullYear(); // Store selected year
    console.log(this.chosenYear);
    this.filterData();  // Update data when year is selected
  }

  // Handles month selection
  chosenMonthHandler(normalizedMonth: Date, datepicker: MatDatepicker<Date>) {
    if (this.chosenYear !== null) {
      this.selectedMonthYear = new Date(this.chosenYear, normalizedMonth.getMonth());
    } else {
      this.selectedMonthYear = new Date(normalizedMonth.getFullYear(), normalizedMonth.getMonth());
    }

    datepicker.close(); // Close the picker after selection
    console.log(this.selectedMonthYear); // Output selected month/year
    this.filterData();  // Update data when month is selected
  }

  // Filter data based on selections (Leave Type, Status, Department)
  filterData() {
    const month = this.selectedMonthYear.getMonth() + 1;  // Month is 0-indexed
    const year = this.selectedMonthYear.getFullYear();

    // Fetch leave statistics for the selected year and month
    this.leaveService.getLeaveStats(year, month).subscribe(stats => {
      this.updateChartData(stats);
    });

    // Fetch leave requests based on filters
    this.leaveService.getLeaveRequests().subscribe(leaveRequests => {
      this.leaveData = leaveRequests
        .filter(leave =>
          (this.selectedLeaveType === 'ทั้งหมด' || leave.leaveType.name === this.selectedLeaveType) &&
          (this.selectedStatus === 'ทั้งหมด' || leave.status === this.selectedStatus) &&
          (this.selectedDepartment === 'ทั้งหมด' || leave.user.department === this.selectedDepartment)
        )

        .map(leave => ({
          ...leave,
          daysTaken: this.calculateDaysTaken(leave.startDate, leave.endDate) // Compute days
        }));

      this.filteredLeaveData = [...this.leaveData];
      this.totalItems = this.leaveData.length;  // Update total item count
    });
  }
  calculateDaysTaken(dayStart: string, dayEnd: string): number {
    if (!dayStart || !dayEnd) {
      console.warn('Missing date values:', { dayStart, dayEnd });
      return 0;
    }

    const startDate = new Date(dayStart);
    const endDate = new Date(dayEnd);

    console.log('Parsed Dates:', { startDate, endDate });

    // Ensure valid date conversion
    if (isNaN(startDate.getTime()) || isNaN(endDate.getTime())) {
      console.error('Invalid date format:', { dayStart, dayEnd });
      return 0;
    }

    // Calculate the difference in milliseconds
    const diffTime = endDate.getTime() - startDate.getTime();
    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24)) + 1;

    console.log('Days Calculated:', { diffDays });

    return diffDays > 0 ? diffDays : 0; // Ensure non-negative days
  }



  // Update chart data based on the leave statistics
  updateChartData(stats: any) {
    this.leaveStatsData = [
      { name: 'ลาป่วย', value: stats['ลาป่วย'] || 0 },
      { name: 'ลาพักร้อน', value: stats['ลาพักร้อน'] || 0 },
      { name: 'ลากิจ', value: stats['ลากิจ'] || 0 },
      { name: 'ลาคลอด', value: stats['ลาคลอด'] || 0 }
    ];
  }
  // Export leave data to Excel
  exportToExcel() {
    const month = this.selectedMonthYear.getMonth() + 1; // Month is 0-indexed
    const year = this.selectedMonthYear.getFullYear();

    this.leaveService.exportLeaveData(year, month).subscribe(base64String => {
      if (!base64String) {
        console.error('Invalid file data');
        return;
      }

      // Convert Base64 to Binary
      const byteCharacters = atob(base64String);
      const byteNumbers = new Array(byteCharacters.length);
      for (let i = 0; i < byteCharacters.length; i++) {
        byteNumbers[i] = byteCharacters.charCodeAt(i);
      }

      const byteArray = new Uint8Array(byteNumbers);
      const blob = new Blob([byteArray], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });

      // Create a downloadable link
      const link = document.createElement('a');
      const fileURL = URL.createObjectURL(blob);
      link.href = fileURL;
      link.download = `LeaveData_${year}_${month}.xlsx`;

      // Append to the body, trigger click, then remove
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);

      // Clean up
      URL.revokeObjectURL(fileURL);
    });
  }





}


