import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/api/v1';

  constructor(private http: HttpClient) {}

  login(payload: loginRequest): Observable<String> {
    return this.http.post(`${this.apiUrl}/users/login`, payload, {
      responseType: 'text',
      withCredentials: true,
    });
  }

  register(payload: registerRequest): Observable<String> {
    return this.http.post(`${this.apiUrl}/users/register`, payload, {
      responseType: 'text',
      withCredentials: true,
    });
  }

  refresh(): Observable<void> {
    return this.http.get<void>(`${this.apiUrl}/refresh`, {
      withCredentials: true,
    });
  }
}
