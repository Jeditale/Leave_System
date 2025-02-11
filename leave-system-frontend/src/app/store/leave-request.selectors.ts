import { createFeatureSelector, createSelector } from '@ngrx/store';
import { State } from './leave-request.reducer';

export const selectLeaveRequestsState = createFeatureSelector<State>('leaveRequests');

export const selectAllLeaveRequests = createSelector(
  selectLeaveRequestsState,
  (state) => state.leaveRequests
);
