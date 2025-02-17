import { Injectable, inject } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { mergeMap, map, catchError, of } from 'rxjs';
import { 
  loadLeaveRequests, loadLeaveRequestsSuccess, loadLeaveRequestsFailure, 
  createLeaveRequest, createLeaveRequestSuccess, createLeaveRequestFailure, 
  approveLeaveRequest, approveLeaveRequestSuccess, approveLeaveRequestFailure, 
  rejectLeaveRequest, rejectLeaveRequestSuccess, rejectLeaveRequestFailure 
} from './leave.actions';
import { LeaveRequestService } from '../services/leave.service';

@Injectable()
export class LeaveRequestEffects {
  private actions$ = inject(Actions);
  private leaveService = inject(LeaveRequestService);

  loadLeaveRequests$ = createEffect(() =>
    this.actions$.pipe(
      ofType(loadLeaveRequests),
      mergeMap(() =>
        this.leaveService.getLeaveRequests().pipe(
          map(leaveRequests => loadLeaveRequestsSuccess({ leaveRequests })),
          catchError(error => of(loadLeaveRequestsFailure({ error })))
        )
      )
    )
  );

  createLeaveRequest$ = createEffect(() =>
    this.actions$.pipe(
      ofType(createLeaveRequest),
      mergeMap(({ createRequest }) =>
        this.leaveService.createLeaveRequest(createRequest).pipe(
          map(response => createLeaveRequestSuccess({ leaveRequest: response })),
          catchError(error => of(createLeaveRequestFailure({ error: error.message })))
        )
      )
    )
  );

  approveLeaveRequest$ = createEffect(() =>
    this.actions$.pipe(
      ofType(approveLeaveRequest),
      mergeMap(({ requestId }) =>
        this.leaveService.approveLeaveRequest(requestId).pipe(
          map(approval => approveLeaveRequestSuccess({ approval, requestId })),
          catchError(error => of(approveLeaveRequestFailure({ error })))
        )
      )
    )
  );

  rejectLeaveRequest$ = createEffect(() =>
    this.actions$.pipe(
      ofType(rejectLeaveRequest),
      mergeMap(({ requestId, comment }) =>
        this.leaveService.rejectLeaveRequest(requestId, comment).pipe(
          map(approval => rejectLeaveRequestSuccess({ approval, requestId })),
          catchError(error => of(rejectLeaveRequestFailure({ error })))
        )
      )
    )
  );
}
