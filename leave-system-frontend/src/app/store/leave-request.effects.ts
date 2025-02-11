import { Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { of } from 'rxjs';
import { catchError, map, switchMap } from 'rxjs/operators';

import { loadLeaveRequests, loadLeaveRequestsSuccess, loadLeaveRequestsFailure, addLeaveRequest, addLeaveRequestSuccess } from './leave-request.actions';
import { LeaveRequestService } from '../../services/leave-request.service';

@Injectable()
export class LeaveRequestEffects {
  constructor(
    private actions$: Actions,
    private leaveRequestService: LeaveRequestService
  ) {}

  loadLeaveRequests$ = createEffect(() =>
    this.actions$.pipe(
      ofType(loadLeaveRequests),
      switchMap(() =>
        this.leaveRequestService.getLeaveRequests().pipe(
          map((leaveRequests) => loadLeaveRequestsSuccess({ leaveRequests })),
          catchError((error) => of(loadLeaveRequestsFailure({ error })))
        )
      )
    )
  );

  addLeaveRequest$ = createEffect(() =>
    this.actions$.pipe(
      ofType(addLeaveRequest),
      switchMap((action) =>
        this.leaveRequestService.addLeaveRequest(action.leaveRequest).pipe(
          map((leaveRequest) => addLeaveRequestSuccess({ leaveRequest })),
          catchError((error) => of(loadLeaveRequestsFailure({ error })))
        )
      )
    )
  );
}
