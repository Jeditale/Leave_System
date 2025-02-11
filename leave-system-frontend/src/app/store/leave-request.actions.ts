import { createAction, props } from '@ngrx/store';
import { LeaveRequest } from './leave-request.model'

export const loadLeaveRequests = createAction('[Leave Request] Load Leave Requests');
export const loadLeaveRequestsSuccess = createAction(
  '[Leave Request] Load Leave Requests Success',
  props<{ leaveRequests: LeaveRequest[] }>()
);
export const loadLeaveRequestsFailure = createAction(
  '[Leave Request] Load Leave Requests Failure',
  props<{ error: any }>()
);

export const addLeaveRequest = createAction(
  '[Leave Request] Add Leave Request',
  props<{ leaveRequest: LeaveRequest }>()
);

export const addLeaveRequestSuccess = createAction(
  '[Leave Request] Add Leave Request Success',
  props<{ leaveRequest: LeaveRequest }>()
);
