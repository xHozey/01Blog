import { inject, Injectable } from '@angular/core';
import { API_URL, API_VERSION } from '../../config';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class FollowService {
  apiUrl = `${API_URL}${API_VERSION}/users`;
  private http = inject(HttpClient);

  getFollowersCount(id: number): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/${id}/followers`, { withCredentials: true });
  }

  getFollowedCount(id: number): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/${id}/followed`, { withCredentials: true });
  }

  followUser(id: number): Observable<string> {
    return this.http.post(`${this.apiUrl}/${id}/follow`, null, {
      withCredentials: true,
      responseType: 'text',
    });
  }

  checkAlreadyFollowed(id: number): Observable<boolean> {
    return this.http.get<boolean>(`${this.apiUrl}/${id}/follow`, {
      withCredentials: true,
    });
  }
}
