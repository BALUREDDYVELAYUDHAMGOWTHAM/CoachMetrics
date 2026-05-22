// features/coach/mentors/mentor-tracker.component.ts
import { Component, OnInit } from '@angular/core';
import { MentorApiService } from '../../../core/services/api.services';
import { MentorResponse, MentorConnectResponse, WEEK_RANGES } from '../../../shared/models';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-mentor-tracker',
  templateUrl: './mentor-tracker.component.html',
  styleUrls: ['./mentor-tracker.component.scss']
})
export class MentorTrackerComponent implements OnInit {
  mentors: MentorResponse[] = [];
  loading = true;
  weekRanges = WEEK_RANGES;

  // Edit connect popup
  editingConnect: MentorConnectResponse | null = null;
  editForm: Partial<MentorConnectResponse> = {};
  saving = false;

  constructor(private mentorApi: MentorApiService, private snack: MatSnackBar) {}

  ngOnInit(): void { this.load(); }

  load(): void {
    this.loading = true;
    this.mentorApi.getMyMentors().subscribe({
      next: d => { this.mentors = d; this.loading = false; },
      error: () => { this.loading = false; this.snack.open('Failed to load', 'Close', { duration: 3000 }); }
    });
  }

  // Get a specific week's connect for a mentor
  getConnect(mentor: MentorResponse, weekNum: number): MentorConnectResponse | undefined {
    return mentor.connects?.find(c => c.weekNumber === weekNum);
  }

  // Cell CSS class
  getCellClass(c: MentorConnectResponse | undefined): string {
    if (!c) return 'cell-empty';
    if (c.happened) return 'cell-done';
    return 'cell-miss';
  }

  openEdit(c: MentorConnectResponse | undefined): void {
    if (!c) return;
    this.editingConnect = c;
    this.editForm = { ...c };
  }

  saveConnect(): void {
    if (!this.editingConnect) return;
    this.saving = true;
    this.mentorApi.updateConnect(this.editingConnect.id, this.editForm).subscribe({
      next: updated => {
        // Update local data
        for (const m of this.mentors) {
          const idx = m.connects?.findIndex(c => c.id === updated.id) ?? -1;
          if (idx >= 0) { m.connects[idx] = updated; }
        }
        this.editingConnect = null;
        this.saving = false;
        this.snack.open('Connect updated', 'Close', { duration: 2000 });
      },
      error: err => { this.saving = false; this.snack.open(err.error?.error ?? 'Save failed', 'Close', { duration: 3000 }); }
    });
  }

  getModeLabel(mode: string | undefined): string {
    const m: Record<string, string> = { VIRTUAL: 'V', IN_PERSON: 'P', HYBRID: 'H', NOT_HAPPENED: '—' };
    return mode ? (m[mode] ?? '—') : '—';
  }

  getModeColor(mode: string | undefined): string {
    const m: Record<string, string> = { VIRTUAL: '#0891B2', IN_PERSON: '#059669', HYBRID: '#D97706', NOT_HAPPENED: '#94A3B8' };
    return mode ? (m[mode] ?? '#94A3B8') : '#94A3B8';
  }

  getTotalHours(mentor: MentorResponse): number {
    return mentor.connects?.filter(c => c.happened).reduce((s, c) => s + (c.hours ?? 0), 0) ?? 0;
  }

  getTotalSessions(mentor: MentorResponse): number {
    return mentor.connects?.filter(c => c.happened).length ?? 0;
  }

  exportCsv(): void {
    // Header row 1: week ranges
    const weekHeader = ['Cohort Code', 'Mentor', 'Status', 'Coach', 'Vertical', ...WEEK_RANGES.flatMap(w => [w + ' - Happened', w + ' - Mode', w + ' - Date', w + ' - Hours', w + ' - Reason'])];

    const rows = this.mentors.map(m => {
      const weekCols = WEEK_RANGES.flatMap((_, i) => {
        const c = this.getConnect(m, i + 1);
        return [c?.happened ? 'Yes' : 'No', c?.mode ?? '', c?.connectDate ?? '', c?.hours ?? '', c?.reason ?? ''];
      });
      return [m.cohortCode ?? '', m.fullName, m.trainingStatus, m.coachName, m.verticalMapping ?? '', ...weekCols];
    });

    const csv = [weekHeader, ...rows].map(r => r.map(v => `"${v}"`).join(',')).join('\n');
    const blob = new Blob([csv], { type: 'text/csv' });
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url; a.download = `CoachMetrics_Tracker_${new Date().toISOString().split('T')[0]}.csv`;
    a.click(); URL.revokeObjectURL(url);
    this.snack.open('CSV exported', 'Close', { duration: 2000 });
  }
}
