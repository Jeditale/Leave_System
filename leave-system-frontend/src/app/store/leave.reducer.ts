import { createReducer, on } from '@ngrx/store';
import { LeaveRequest } from '../models/leave-request.model';
import { loadLeaveRequestsSuccess, loadLeaveRequestsFailure, createLeaveRequestSuccess, createLeaveRequestFailure, approveLeaveRequestSuccess, approveLeaveRequestFailure, rejectLeaveRequestSuccess, rejectLeaveRequestFailure, createLeaveRequest } from './leave.actions';

export interface LeaveRequestState {
  leaveRequests: LeaveRequest[];
  loading: boolean;
  error: string | null;
}

export const initialState: LeaveRequestState = {
  leaveRequests: [],
  loading: false,
  error: null
};

export const leaveRequestReducer = createReducer(
  initialState,
  // Load leave requests actions
  on(loadLeaveRequestsSuccess, (state, { leaveRequests }) => ({
    ...state,
    leaveRequests,
    loading: false
  })),
  on(loadLeaveRequestsFailure, (state, { error }) => ({
    ...state,
    error,
    loading: false
  })),

  on(createLeaveRequest, (state, { }) => ({
    ...state,
    // Optionally, add the new leave request to the state or handle loading
  })),
  on(createLeaveRequestSuccess, (state, { leaveRequest }) => ({
    ...state,
    leaveRequests: [...state.leaveRequests, leaveRequest],
  })),
  on(createLeaveRequestFailure, (state, { error }) => ({
    ...state,
    error
  })),

  on(approveLeaveRequestSuccess, (state, { approval, requestId }) => ({
    ...state,
    leaveRequests: state.leaveRequests.map(req =>
      req.id === requestId ? { ...req, status: 'Approved', comment: approval.comment } : req
    )
  })),

  on(rejectLeaveRequestSuccess, (state, { approval, requestId }) => ({
    ...state,
    leaveRequests: state.leaveRequests.map(req =>
      req.id === requestId ? { ...req, status: 'Rejected', comment: approval.comment } : req
    )
  })),

  on(rejectLeaveRequestFailure, (state, { error }) => ({
    ...state,
    error
  }))
);
