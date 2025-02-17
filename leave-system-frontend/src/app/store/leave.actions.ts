import { createAction, props } from '@ngrx/store';
import { LeaveRequest } from '../models/leave-request.model';
import { Approval } from '../models/leave-request-approval.model';
import { CreateRequest } from '../models/create-requset-form.model';

// Load leave requests actions
export const loadLeaveRequests = createAction('[LeaveRequest] Load Leave Requests');
export const loadLeaveRequestsSuccess = createAction(
  '[LeaveRequest] Load Leave Requests Success',
  props<{ leaveRequests: LeaveRequest[] }>()
);
export const loadLeaveRequestsFailure = createAction(
  '[LeaveRequest] Load Leave Requests Failure',
  props<{ error: any }>()
);

// Create leave request actions
export const createLeaveRequest = createAction(
  '[LeaveRequest] Create Leave Request',
  props<{ createRequest: CreateRequest }>()
);

export const createLeaveRequestSuccess = createAction(
  '[LeaveRequest] Create Leave Request Success',
  props<{ leaveRequest: CreateRequest }>()
);

export const createLeaveRequestFailure = createAction(
  '[LeaveRequest] Create Leave Request Failure',
  props<{ error: any }>()
);

// Approve leave request actions
export const approveLeaveRequest = createAction(
  '[LeaveRequest] Approve Leave Request',
  props<{ requestId: number , comment:string}>()
);
export const approveLeaveRequestSuccess = createAction(
  '[LeaveRequest] Approve Leave Request Success',
  props<{ approval: Approval, requestId: number  }>()
);
export const approveLeaveRequestFailure = createAction(
  '[LeaveRequest] Approve Leave Request Failure',
  props<{ error: any }>()
);

// Reject leave request actions
export const rejectLeaveRequest = createAction(
  '[LeaveRequest] Reject Leave Request',
  props<{ requestId: number, comment: string }>()
);
export const rejectLeaveRequestSuccess = createAction(
  '[LeaveRequest] Reject Leave Request Success',
  props<{ approval: Approval, requestId: number }>()
);
export const rejectLeaveRequestFailure = createAction(
  '[LeaveRequest] Reject Leave Request Failure',
  props<{ error: any }>()
);
