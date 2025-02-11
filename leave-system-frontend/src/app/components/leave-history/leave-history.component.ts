import { Component, Renderer2, AfterViewInit } from '@angular/core';

@Component({
  selector: 'app-leave-history',
  templateUrl: './leave-history.component.html',
  styleUrls: ['./leave-history.component.scss']
})
export class LeaveHistoryComponent implements AfterViewInit {
  // displayedColumns: string[] = ['userName', 'leaveType', 'status', 'details'];
  // leaveHistory = [
  //   { userName: 'John Doe', leaveType: 'Sick', status: 'Approved' },
  //   { userName: 'Jane Smith', leaveType: 'Vacation', status: 'Pending' }
  // ];

  // dates = [
  //   { date: "2025-02-01", text: "Special Day 1" ,username:"jay",leaveType:"sick-leave"},
  //   { date: "2025-02-20", text: "Special Day 2" ,username:"jay",leaveType:"sick-leave"}
  // ];

  // Today = [
  //   { date: this.dateToString(new Date()), text: "Today" }
  // ];

  // constructor(private renderer: Renderer2) {}

  ngAfterViewInit() {
    // this.streamOpened();
  }

  // dateClass = (d: Date) => {
  //   const dateSearch = this.dateToString(d);
  //   if (this.Today.find(f => f.date === dateSearch)) {
  //     return "todays_class";
  //   } else if (this.dates.find(f => f.date === dateSearch)) {
  //     return "example-custom-date-class";
  //   } else {
  //     return "normal";
  //   }
  // };

  // displayMonth() {
  //   setTimeout(() => {
  //     let x = document.querySelectorAll(".mat-calendar-body-cell");
  //     x.forEach(y => {
  //       const ariaLabel = y.getAttribute("aria-label");
  //       if (ariaLabel) {
  //         const dateSearch = this.dateToString(new Date(ariaLabel));
  //         const data = this.dates.find(f => f.date === dateSearch);
  //         const data_today = this.Today.find(f => f.date === dateSearch);

  //         if (data) y.setAttribute("aria-label", data.text);
  //         if (data_today) y.setAttribute("aria-label", data_today.text);
  //       }
  //     });
  //   });
  // }

  // streamOpened() {
  //   setTimeout(() => {
  //     let buttons = document.querySelectorAll("mat-calendar .mat-icon-button");

  //     buttons.forEach(btn =>
  //       this.renderer.listen(btn, "click", () => {
  //         setTimeout(() => {
  //           this.displayMonth();
  //         });
  //       })
  //     );
  //     this.displayMonth();
  //   });
  // }

  // dateToString(date: Date): string {
  //   return (
  //     date.getFullYear() +
  //     "-" +
  //     ("0" + (date.getMonth() + 1)).slice(-2) +
  //     "-" +
  //     ("0" + date.getDate()).slice(-2)
  //   );
  // }
}
