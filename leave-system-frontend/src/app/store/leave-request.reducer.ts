import { createReducer, on } from '@ngrx/store';
import { loadLeaveRequestsSuccess, loadLeaveRequestsFailure, addLeaveRequestSuccess } from './leave-request.actions';
import { LeaveRequest } from './leave-request.model';

export interface State {
  leaveRequests: LeaveRequest[];
  error: string | null;
}

export const initialState: State = {
  leaveRequests: [],
  error: null
};

export const leaveRequestReducer = createReducer(
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
  on(addLeaveRequestSuccess, (state, { leaveRequest }) => ({
    ...state,
    leaveRequests: [...state.leaveRequests, leaveRequest]
  }))
);
