import { NgModule } from '@angular/core';
import { BrowserModule, provideClientHydration } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { StoreModule } from '@ngrx/store';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { LeaveRequestFormComponent } from './components/leave-request-form/leave-request-form.component';
import { LeaveApprovalComponent } from './components/leave-approval/leave-approval.component';
import { LeaveHistoryComponent } from './components/leave-history/leave-history.component';
import { EffectsModule } from '@ngrx/effects';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatTableModule } from '@angular/material/table';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatListModule } from '@angular/material/list';
import { MatTabsModule } from '@angular/material/tabs';
import { MatToolbarModule } from '@angular/material/toolbar';
import { NativeDateAdapter, MAT_DATE_FORMATS, MAT_DATE_LOCALE, DateAdapter } from '@angular/material/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatDialogModule } from '@angular/material/dialog';
import { MatPaginatorModule } from '@angular/material/paginator';
import { NgxChartsModule } from '@swimlane/ngx-charts';
import { leaveReducer } from './store/leave.reducer';
import { LeaveEffects } from './store/leave.effects';
import { HttpClientModule, provideHttpClient, withFetch } from '@angular/common/http';
import { LeaveRequestService } from './services/leave.service';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

export const MY_DATE_FORMATS = {
  display: {
    dateInput: 'DD/MM/YYYY',
    monthYearLabel: 'MMM YYYY',
    dateA11yLabel: 'DD/MM/YYYY',
    monthYearA11yLabel: 'MMMM YYYY'
  }
};

@NgModule({
  declarations: [
    AppComponent,
    DashboardComponent,
    LeaveRequestFormComponent,
    LeaveApprovalComponent,
    LeaveHistoryComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    StoreModule.forRoot({}, {}),
    EffectsModule.forRoot([]),
    MatButtonModule,
    MatCardModule,  // Ensure MatCardModule is imported here
    MatIconModule,
    MatTableModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatDatepickerModule,
    MatListModule,
    MatTabsModule,
    MatToolbarModule,
    FormsModule,
    ReactiveFormsModule,
    MatDialogModule,
    MatPaginatorModule,
    NgxChartsModule,
    StoreModule.forRoot({ leave: leaveReducer }),
    EffectsModule.forRoot([LeaveEffects]),
    HttpClientModule,
    EffectsModule.forFeature([LeaveEffects]),
    MatProgressSpinnerModule



  ],
  providers: [
    { provide: MAT_DATE_FORMATS, useValue: MY_DATE_FORMATS },
    { provide: MAT_DATE_LOCALE, useValue: 'en-GB' },
    { provide: DateAdapter, useClass: NativeDateAdapter },
    provideClientHydration(),
    provideAnimationsAsync(),
    provideHttpClient(withFetch()),
    LeaveRequestService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
