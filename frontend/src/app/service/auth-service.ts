import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { API_URL, API_VERSION } from '../../config';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private apiUrl = `${API_URL}${API_VERSION}/auth`;

  constructor(private http: HttpClient) {}

  login(payload: userRequest): Observable<string> {
    return this.http.post(`${this.apiUrl}/login`, payload, {
      withCredentials: true,
      responseType: 'text',
    });
  }

  register(payload: userRequest): Observable<string> {
    return this.http.post(`${this.apiUrl}/register`, payload, {
      withCredentials: true,
      responseType: 'text',
    });
  }
}
