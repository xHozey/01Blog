import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { NavbarComponent } from '../../components/navbar-component/navbar-component';

@Component({
  selector: 'app-admin-dashboard',
  imports: [CommonModule, NavbarComponent],
  templateUrl: './admin-dashboard.html',
  styleUrls: ['./admin-dashboard.css'],
})
export class AdminDashboard {
  activeTab: 'users' | 'userReports' = 'users';

  users = [
    { username: 'sarahj', email: 'sarah@example.com', role: 'user', status: 'active' },
    { username: 'mikechen', email: 'mike@example.com', role: 'user', status: 'active' },
    { username: 'emilyd', email: 'emily@example.com', role: 'moderator', status: 'active' },
  ];

  userReports = [];

  setTab(tab: 'users' | 'userReports') {
    this.activeTab = tab;
  }
}
