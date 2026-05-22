import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CoachResponse, CoachRequest, MentorResponse, MentorRequest,
         MentorConnectResponse, DashboardStats, ReportRow } from '../../shared/models';
import { environment } from '../../../environments/environment';

const API = environment.apiUrl;

@Injectable({ providedIn: 'root' })
export class CoachApiService {
  constructor(private readonly http: HttpClient) {}

  getCoaches(name?: string, dept?: string, active?: boolean): Observable<CoachResponse[]> {
    let params = new HttpParams();
    if (name)             params = params.set('name', name);
    if (dept)             params = params.set('dept', dept);
    if (active !== undefined) params = params.set('active', String(active));
    return this.http.get<CoachResponse[]>(`${API}/admin/coaches`, { params });
  }

  createCoach(req: CoachRequest): Observable<CoachResponse> {
    return this.http.post<CoachResponse>(`${API}/admin/coaches`, req);
  }

  updateCoach(id: number, req: CoachRequest): Observable<CoachResponse> {
    return this.http.put<CoachResponse>(`${API}/admin/coaches/${id}`, req);
  }

  deleteCoach(id: number): Observable<void> {
    return this.http.delete<void>(`${API}/admin/coaches/${id}`);
  }
}

@Injectable({ providedIn: 'root' })
export class MentorApiService {
  constructor(private readonly http: HttpClient) {}

  getMyMentors(name?: string, dept?: string, cohort?: string): Observable<MentorResponse[]> {
    let params = new HttpParams();
    if (name)   params = params.set('name', name);
    if (dept)   params = params.set('dept', dept);
    if (cohort) params = params.set('cohort', cohort);
    return this.http.get<MentorResponse[]>(`${API}/coach/mentors`, { params });
  }

  getAllMentors(name?: string, dept?: string): Observable<MentorResponse[]> {
    let params = new HttpParams();
    if (name) params = params.set('name', name);
    if (dept) params = params.set('dept', dept);
    return this.http.get<MentorResponse[]>(`${API}/admin/mentors`, { params });
  }

  getMentorById(id: number): Observable<MentorResponse> {
    return this.http.get<MentorResponse>(`${API}/coach/mentors/${id}`);
  }

  createMentor(req: MentorRequest): Observable<MentorResponse> {
    return this.http.post<MentorResponse>(`${API}/coach/mentors`, req);
  }

  updateMentor(id: number, req: MentorRequest): Observable<MentorResponse> {
    return this.http.put<MentorResponse>(`${API}/coach/mentors/${id}`, req);
  }

  deleteMentor(id: number): Observable<void> {
    return this.http.delete<void>(`${API}/coach/mentors/${id}`);
  }

  updateConnect(connectId: number, req: Partial<MentorConnectResponse>): Observable<MentorConnectResponse> {
    return this.http.put<MentorConnectResponse>(`${API}/coach/connects/${connectId}`, req);
  }
}

@Injectable({ providedIn: 'root' })
export class DashboardApiService {
  constructor(private readonly http: HttpClient) {}
  getAdminDashboard(): Observable<DashboardStats>   { return this.http.get<DashboardStats>(`${API}/admin/dashboard`); }
  getCoachDashboard(): Observable<DashboardStats>   { return this.http.get<DashboardStats>(`${API}/coach/dashboard`); }
  getReport(dept?: string, period?: string): Observable<ReportRow[]> {
    let params = new HttpParams();
    if (dept)   params = params.set('dept', dept);
    if (period) params = params.set('period', period);
    return this.http.get<ReportRow[]>(`${API}/admin/reports`, { params });
  }
}
