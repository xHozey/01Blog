import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { API_URL, API_VERSION } from '../../config';

@Injectable({
  providedIn: 'root',
})
export class AdminService {
  private apiUrl = `${API_URL}${API_VERSION}/admin`;
  private http = inject(HttpClient);

  deletePost(id: number): Observable<string> {
    return this.http.delete(`${this.apiUrl}/post/${id}/delete`, {
      withCredentials: true,
      responseType: 'text',
    });
  }

  toggleHidePost(id: number): Observable<string> {
    return this.http.post(`${this.apiUrl}/post/${id}/hide`, null, {
      withCredentials: true,
      responseType: 'text',
    });
  }

  deleteComment(id: number): Observable<string> {
    return this.http.delete(`${this.apiUrl}/comment/${id}/delete`, {
      withCredentials: true,
      responseType: 'text',
    });
  }

  getPosts(page: number): Observable<adminPostDTO[]> {
    return this.http.get<adminPostDTO[]>(`${this.apiUrl}/post?page=${page}`, {
      withCredentials: true,
    });
  }

  getUsers(page: number): Observable<adminUserDTO[]> {
    return this.http.get<adminUserDTO[]>(`${this.apiUrl}/user?page=${page}`, {
      withCredentials: true,
    });
  }

  getBannedUsers(page: number): Observable<adminUserDTO[]> {
    return this.http.get<adminUserDTO[]>(`${this.apiUrl}/user/banned?page=${page}`, {
      withCredentials: true,
    });
  }

  toggleBanUser(id: number): Observable<string> {
    return this.http.post(`${this.apiUrl}/user/${id}/ban`, null, {
      withCredentials: true,
      responseType: 'text',
    });
  }

  deleteUser(id: number): Observable<string> {
    return this.http.delete(`${this.apiUrl}/user/${id}/delete`, {
      withCredentials: true,
      responseType: 'text',
    });
  }

  getUserReports(page: number): Observable<reportUser[]> {
    return this.http.get<reportUser[]>(`${this.apiUrl}/report/user?page=${page}`, {
      withCredentials: true,
    });
  }
  getPostReports(page: number): Observable<postReportDTO[]> {
    return this.http.get<postReportDTO[]>(`${this.apiUrl}/report/post?page=${page}`, {
      withCredentials: true,
    });
  }
  getTotalUsers(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/stats/user`, {
      withCredentials: true,
    });
  }

  getTotalPosts(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/stats/post`, {
      withCredentials: true,
    });
  }

  getTotalUserReports(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/stats/report/user`, {
      withCredentials: true,
    });
  }

  getTotalPostReports(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/stats/report/post`, {
      withCredentials: true,
    });
  }

  getTotalBan(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/stats/ban`, {
      withCredentials: true,
    });
  }
}
