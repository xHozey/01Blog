import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class Auth {
  private apiUrl = 'http://localhost:8080/api/v1/users';

  constructor(private http: HttpClient) {}

  login(payload: loginRequest): Observable<String> {
    return this.http.post(`${this.apiUrl}/login`, payload, { responseType: 'text', withCredentials: true });
  }

  register(payload: registerRequest): Observable<String> {
    return this.http.post(`${this.apiUrl}/register`, payload, { responseType: 'text', withCredentials: true });
  }
}
