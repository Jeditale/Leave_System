import { Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { LeaveRequestService } from '../services/leave.service';
import {
  loadLeaveRequests,
  loadLeaveRequestsSuccess,
  loadLeaveRequestsFailure,
  approveLeaveRequest,
  rejectLeaveRequest
} from './leave.actions';
import { catchError, map, mergeMap } from 'rxjs/operators';
import { of } from 'rxjs';

@Injectable()
export class LeaveEffects {

  loadLeaveRequests$ = createEffect(() =>
    this.actions$.pipe(
      ofType(loadLeaveRequests), // FIXED: Removed `LeaveActions.`
      mergeMap(() =>
        this.leaveService.getLeaveRequests().pipe(
          map(leaveRequests => loadLeaveRequestsSuccess({ leaveRequests })),
          catchError(error => of(loadLeaveRequestsFailure({ error })))
        )
      )
    )
  );

  approveLeaveRequest$ = createEffect(() =>
    this.actions$.pipe(
      ofType(approveLeaveRequest),
      mergeMap(action =>
        this.leaveService.approveLeaveRequest(action.requestId).pipe(
          map(() => loadLeaveRequests()), // FIXED: Reload leave requests after update
          catchError(error => of(loadLeaveRequestsFailure({ error }))) // FIXED: Ensure import
        )
      )
    )
  );

  rejectLeaveRequest$ = createEffect(() =>
    this.actions$.pipe(
      ofType(rejectLeaveRequest),
      mergeMap(action =>
        this.leaveService.rejectLeaveRequest(action.requestId, action.comment).pipe(
          map(() => loadLeaveRequests()), // FIXED: Reload leave requests after update
          catchError(error => of(loadLeaveRequestsFailure({ error }))) // FIXED: Ensure import
        )
      )
    )
  );

  constructor(
    private readonly actions$: Actions,
    private readonly leaveService: LeaveRequestService, // FIXED: Ensure this service is injected

  ) {}
}
