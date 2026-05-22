// layout/main-layout.component.ts
import { Component, OnInit } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';
import { filter } from 'rxjs/operators';
import { AuthService } from '../core/services/auth.service';
import { AuthResponse } from '../shared/models';

@Component({
  selector: 'app-main-layout',
  templateUrl: './main-layout.component.html',
  styleUrls: ['./main-layout.component.scss']
})
export class MainLayoutComponent implements OnInit {
  currentUser: AuthResponse | null = null;
  currentRoute = '';

  adminNav = [
    { path: '/admin/dashboard', icon: 'grid_view',       label: 'Dashboard' },
    { path: '/admin/coaches',   icon: 'supervisor_account', label: 'Coach Mgmt' },
    { path: '/admin/mentors',   icon: 'people',          label: 'Mentor List' },
    { path: '/admin/reports',   icon: 'bar_chart',       label: 'Reports' },
  ];

  coachNav = [
    { path: '/coach/dashboard', icon: 'grid_view',       label: 'Dashboard' },
    { path: '/coach/mentors',   icon: 'people',          label: 'Mentor Mgmt' },
    { path: '/coach/tracker',   icon: 'table_chart',     label: 'Weekly Tracker' },
  ];

  constructor(public auth: AuthService, private router: Router) {}

  ngOnInit(): void {
    this.currentUser = this.auth.getCurrentUser();
    this.router.events.pipe(filter(e => e instanceof NavigationEnd))
      .subscribe((e: any) => this.currentRoute = e.urlAfterRedirects);
    this.currentRoute = this.router.url;
    if (this.router.url === '/') {
      this.router.navigate([this.auth.isAdmin() ? '/admin/dashboard' : '/coach/dashboard']);
    }
  }

  get navLinks() { return this.auth.isAdmin() ? this.adminNav : this.coachNav; }
  isActive(path: string): boolean { return this.currentRoute.startsWith(path); }
  navigate(path: string): void { this.router.navigate([path]); }
  logout(): void { this.auth.logout(); }
}
