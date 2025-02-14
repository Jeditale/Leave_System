import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LeaveRequestService {
  private baseUrl = 'http://localhost:5300/api/leave-requests';

  constructor(private http: HttpClient) {}

  // GET all leave requests
  getLeaveRequests(): Observable<any[]> {

    return this.http.get<any[]>(`${this.baseUrl}`);
  }
  getLeaveRequestsByUserId(userId: number): Observable<any[]> {

    return this.http.get<any[]>(`${this.baseUrl}/user/${userId}`);
  }
  countPendingByUserId(userId: number): Observable<number> {

    return this.http.get<number>(`${this.baseUrl}/pending/count/${userId}`);
  }


  // Create a new leave request
  createLeaveRequest(leaveData: any): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}`, leaveData);
  }

  // Approve a leave request
  approveLeaveRequest(requestId: number): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}/approve/${requestId}`, {});
  }

  // Reject a leave request with a comment
  rejectLeaveRequest(requestId: number, comment: string): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}/reject/${requestId}`, { comment });
  }

  // Get leave balance for a user
  getLeaveBalance(userId: number): Observable<number> {
    return this.http.get<number>(`${this.baseUrl}/balance/${userId}`);
  }

  // Get pending leave requests
  getPendingLeaveRequests(): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/pending`);
  }

  // Get leave statistics for a given year and month
  getLeaveStats(year: number, month: number): Observable<any> {
    return this.http.get<any>(`${this.baseUrl}/stats/${year}/${month}`);
  }

  // Export leave data for a specific month as Base64 (Excel file)
  exportLeaveData(year: number, month: number): Observable<string> {
    return this.http.get(`http://localhost:5300/api/leave-requests/export/${year}/${month}`, {
      responseType: 'text'  // 👈 Important! Expecting a Base64 string, not JSON.
    });
  }

}

