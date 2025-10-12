import { inject, Injectable } from '@angular/core';
import { API_URL, API_VERSION } from '../../config';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class RefreshService {
  private apiUrl = `${API_URL}${API_VERSION}/refresh`;
  private http = inject(HttpClient);

  refresh(): Observable<string> {
    return this.http.get(`${this.apiUrl}`, { withCredentials: true, responseType: 'text' });
  }
}
