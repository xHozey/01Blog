import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { NavbarComponent } from "../../components/navbar-component/navbar-component";

@Component({
  selector: 'app-admin-dashboard',
  imports: [CommonModule, NavbarComponent],
  templateUrl: './admin-dashboard.html',
  styleUrls: ['./admin-dashboard.css'],
})
export class AdminDashboard {
  activeTab: 'users' | 'postReports' | 'commentReports' = 'users';

  users = [
    { username: 'sarahj', email: 'sarah@example.com', role: 'user', status: 'active' },
    { username: 'mikechen', email: 'mike@example.com', role: 'user', status: 'active' },
    { username: 'emilyd', email: 'emily@example.com', role: 'moderator', status: 'active' },
  ];

  postReports = [
    { postId: 101, reporter: 'john', reason: 'Inappropriate content', date: new Date() },
    { postId: 102, reporter: 'anna', reason: 'Spam', date: new Date() },
  ];

  commentReports = [
    { commentId: 501, reporter: 'lucas', reason: 'Harassment', date: new Date() },
    { commentId: 502, reporter: 'kate', reason: 'Offensive language', date: new Date() },
  ];

  userReports = []

  setTab(tab: 'users' | 'postReports' | 'commentReports' | 'userReports') {
    this.activeTab = tab;
  }
}
