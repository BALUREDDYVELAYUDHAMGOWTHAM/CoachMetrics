// features/admin/dashboard/admin-dashboard.component.ts
import { Component, OnInit } from '@angular/core';
import { DashboardApiService } from '../../../core/services/api.services';
import { DashboardStats } from '../../../shared/models';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-admin-dashboard',
  templateUrl: './admin-dashboard.component.html',
  styleUrls: ['./admin-dashboard.component.scss']
})
export class AdminDashboardComponent implements OnInit {
  stats: DashboardStats | null = null;
  loading = true;
  kpis: { label: string; value: string | number; icon: string; iconBg: string; iconColor: string }[] = [];

  constructor(private dashApi: DashboardApiService, private snack: MatSnackBar) {}

  ngOnInit(): void { this.load(); }

  load(): void {
    this.loading = true;
    this.dashApi.getAdminDashboard().subscribe({
      next: d => {
        this.stats = d;
        const hrs = d.totalHours ?? 0;
        this.kpis = [
          { label: 'Total Mentors',  value: d.totalMentors  ?? 0, icon: 'people',             iconBg: '#FEF3C7', iconColor: '#D97706' },
          { label: 'Active Coaches', value: d.activeCoaches ?? 0, icon: 'supervisor_account', iconBg: '#DBEAFE', iconColor: '#1D4ED8' },
          { label: 'SDET Dept.',     value: d.sdetCount     ?? 0, icon: 'code',               iconBg: '#EDE9FE', iconColor: '#6D28D9' },
          { label: '.NET/C# Dept.',  value: d.dotnetCount   ?? 0, icon: 'terminal',           iconBg: '#CFFAFE', iconColor: '#0891B2' },
          { label: 'Total Sessions', value: d.totalSessions ?? 0, icon: 'event_available',    iconBg: '#DCFCE7', iconColor: '#059669' },
          { label: 'Total Hours',    value: hrs.toFixed(1),        icon: 'schedule',           iconBg: '#FEF3C7', iconColor: '#D97706' },
        ];
        this.loading = false;
      },
      error: () => {
        this.loading = false;
        this.snack.open('Failed to load dashboard', 'Close', { duration: 3000 });
      }
    });
  }

  getDeptBars(): { name: string; count: number; color: string; pct: number }[] {
    if (!this.stats?.deptDistribution) return [];
    const total = Object.values(this.stats.deptDistribution).reduce((a, b) => a + b, 0) || 1;
    return Object.entries(this.stats.deptDistribution).map(([name, count]) => ({
      name, count,
      pct: Math.round((count / total) * 100),
      color: name === 'SDET' ? '#1D4ED8' : '#0891B2'
    }));
  }

  getActivityIcon(type: string): string {
    const m: Record<string, string> = {
      COACH_ADDED: 'person_add', COACH_UPDATED: 'edit', COACH_DELETED: 'person_remove',
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
