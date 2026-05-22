// features/coach/dashboard/coach-dashboard.component.ts
import { Component, OnInit } from '@angular/core';
import { DashboardApiService } from '../../../core/services/api.services';
import { DashboardStats } from '../../../shared/models';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-coach-dashboard',
  templateUrl: './coach-dashboard.component.html',
  styleUrls: ['./coach-dashboard.component.scss']
})
export class CoachDashboardComponent implements OnInit {
  stats: DashboardStats | null = null;
  loading = true;
  kpis: { label: string; value: string | number; icon: string; iconBg: string; iconColor: string }[] = [];

  constructor(private dashApi: DashboardApiService, private snack: MatSnackBar) {}

  ngOnInit(): void { this.load(); }

  load(): void {
    this.loading = true;
    this.dashApi.getCoachDashboard().subscribe({
      next: d => {
        this.stats = d;
        const hrs = d.totalHours ?? 0;
        this.kpis = [
          { label: 'My Mentors',          value: d.myMentors          ?? 0, icon: 'people',          iconBg: '#FEF3C7', iconColor: '#D97706' },
          { label: 'Sessions This Month',  value: d.sessionsThisMonth  ?? 0, icon: 'event_available', iconBg: '#DCFCE7', iconColor: '#059669' },
          { label: 'Upcoming Sessions',    value: d.upcomingSessions   ?? 0, icon: 'upcoming',        iconBg: '#DBEAFE', iconColor: '#1D4ED8' },
          { label: 'Cohorts Assigned',     value: d.cohortsAssigned    ?? 0, icon: 'group_work',      iconBg: '#EDE9FE', iconColor: '#6D28D9' },
          { label: 'Total Sessions',       value: d.totalSessions      ?? 0, icon: 'schedule',        iconBg: '#CFFAFE', iconColor: '#0891B2' },
          { label: 'Total Hours',          value: hrs.toFixed(1),             icon: 'timer',           iconBg: '#FEF3C7', iconColor: '#D97706' },
        ];
        this.loading = false;
      },
      error: () => {
        this.loading = false;
        this.snack.open('Failed to load dashboard', 'Close', { duration: 3000 });
      }
    });
  }

  getActivityIcon(type: string): string {
    const m: Record<string, string> = {
      MENTOR_ADDED: 'group_add', MENTOR_UPDATED: 'edit_note', MENTOR_DELETED: 'group_remove',
      CONNECT_UPDATED: 'event_available'
    };
    return m[type] ?? 'info';
  }

  getActivityDot(type: string): string {
    if (type.includes('DELETED')) return 'red';
    if (type.includes('ADDED'))   return 'green';
    return 'amber';
  }
}
