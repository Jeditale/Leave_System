import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { LeaveRequest } from '../models/leave-request.model';
import { Approval } from '../models/leave-request-approval.model';

@Injectable({
  providedIn: 'root'
})
export class LeaveRequestService {
  private baseUrl = 'http://localhost:5300/api/leave-requests';

  constructor(private http: HttpClient) {}

  // GET all leave requests
  getLeaveRequests(): Observable<LeaveRequest[]> {
    return this.http.get<LeaveRequest[]>(`${this.baseUrl}`);
  }

  getLeaveRequestsByUserId(userId: number): Observable<LeaveRequest[]> {
    return this.http.get<LeaveRequest[]>(`${this.baseUrl}/user/${userId}`);
  }

  countPendingByUserId(userId: number): Observable<number> {
    return this.http.get<number>(`${this.baseUrl}/pending/count/${userId}`);
  }

  createLeaveRequest(leaveData: any): Observable<LeaveRequest> {
    return this.http.post<LeaveRequest>(`${this.baseUrl}`, leaveData);
  }

  approveLeaveRequest(requestId: number): Observable<Approval> {
    return this.http.post<Approval>(`${this.baseUrl}/approve/${requestId}`, {});
  }

  rejectLeaveRequest(requestId: number, comment: string): Observable<Approval> {
    return this.http.post<Approval>(`${this.baseUrl}/reject/${requestId}`, { comment });
  }

  getLeaveBalance(userId: number): Observable<number> {
    return this.http.get<number>(`${this.baseUrl}/balance/${userId}`);
  }

  getPendingLeaveRequests(): Observable<Approval[]> {
    return this.http.get<LeaveRequest[]>(`${this.baseUrl}/pending`);
  }

  getLeaveStats(year: number, month: number): Observable<any> {
    return this.http.get<any>(`${this.baseUrl}/stats/${year}/${month}`);
  }

  exportLeaveData(year: number, month: number): Observable<string> {
    return this.http.get(`http://localhost:5300/api/leave-requests/export/${year}/${month}`, {
      responseType: 'text'  // Important! Expecting a Base64 string, not JSON.
    });
  }
  getLeaveStatsTable(userId: number, month: number, year: number): Observable<any> {
    return this.http.get<any>(`${this.baseUrl}/stats/${userId}/${month}/${year}`);
  }

}

