import { inject, Injectable } from '@angular/core';
import { API_URL, API_VERSION } from '../../config';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class NotificationsService {
  apiUrl = `${API_URL}${API_VERSION}/notifications`;

  private http = inject(HttpClient);

  getNotifications(page: number): Observable<notificationDTO[]> {
    return this.http.get<notificationDTO[]>(`${this.apiUrl}?page=${page}`, {
      withCredentials: true,
    });
  }

  getNotificationCount(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/count`, {
      withCredentials: true,
    });
  }

  readNotification(ids: number[]): Observable<string> {
    return this.http.post(`${this.apiUrl}/read`, ids, {
      withCredentials: true,
      responseType: 'text',
    });
  }
}
