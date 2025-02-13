import { createReducer, on } from '@ngrx/store';
import { loadLeaveRequestsSuccess, loadLeaveRequestsFailure, approveLeaveRequest, rejectLeaveRequest } from './leave.actions';

export interface LeaveState {
  leaveRequests: any[];
  error: string | null;
}

export const initialState: LeaveState = {
  leaveRequests: [],
  error: null
};

export const leaveReducer = createReducer(
  initialState,
  on(loadLeaveRequestsSuccess, (state, { leaveRequests }) => ({
    ...state,
    leaveRequests,
    error: null
  })),
  on(loadLeaveRequestsFailure, (state, { error }) => ({
    ...state,
    error
  })),
  on(approveLeaveRequest, (state, { requestId }) => ({
    ...state,
    leaveRequests: state.leaveRequests.map(request =>
      request.id === requestId ? { ...request, status: 'Approved' } : request
    )
  })),
  on(rejectLeaveRequest, (state, { requestId }) => ({
    ...state,
    leaveRequests: state.leaveRequests.map(request =>
      request.id === requestId ? { ...request, status: 'Rejected' } : request
    )
  }))
);
