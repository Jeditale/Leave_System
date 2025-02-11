import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { LeaveRequest } from '../models/leave-request.model';

@Injectable({
  providedIn: 'root',
})
export class LeaveRequestService {
  private apiUrl = '/api/leave-requests';

  constructor(private http: HttpClient) {}

  getLeaveRequests(): Observable<LeaveRequest[]> {
    return this.http.get<LeaveRequest[]>(this.apiUrl);
  }

  addLeaveRequest(leaveRequest: LeaveRequest): Observable<LeaveRequest> {
    return this.http.post<LeaveRequest>(this.apiUrl, leaveRequest);
  }
}
