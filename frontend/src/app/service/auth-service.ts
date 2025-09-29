import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/api/v1';

  constructor(private http: HttpClient) {}

  login(payload: userRequest): Observable<string> {
    return this.http.post(`${this.apiUrl}/users/login`, payload, {
      withCredentials: true,
      responseType: 'text',
    });
  }

  register(payload: userRequest): Observable<string> {
    return this.http.post(`${this.apiUrl}/users/register`, payload, {
      withCredentials: true,
      responseType: 'text',
    });
  }
}
