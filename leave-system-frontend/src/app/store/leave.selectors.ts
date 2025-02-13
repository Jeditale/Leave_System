import { createFeatureSelector, createSelector } from '@ngrx/store';
import { LeaveState } from './leave.reducer';

export const selectLeaveState = createFeatureSelector<LeaveState>('leave');

export const selectLeaveRequests = createSelector(
  selectLeaveState,
  (state: LeaveState) => state.leaveRequests
);
