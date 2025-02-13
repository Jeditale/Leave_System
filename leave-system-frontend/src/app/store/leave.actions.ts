import { createAction, props } from '@ngrx/store';

export const loadLeaveRequests = createAction(
  '[Leave] Load Leave Requests'
);

export const loadLeaveRequestsSuccess = createAction(
  '[Leave] Load Leave Requests Success',
  props<{ leaveRequests: any[] }>()
);

export const loadLeaveRequestsFailure = createAction(
  '[Leave] Load Leave Requests Failure',
  props<{ error: any }>()
);

export const approveLeaveRequest = createAction(
  '[Leave] Approve Leave Request',
  props<{ requestId: number }>()
);

export const rejectLeaveRequest = createAction(
  '[Leave] Reject Leave Request',
  props<{ requestId: number, comment: string }>()
);
