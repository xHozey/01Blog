import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class Auth {
  private apiUrl = 'http://localhost:8080/api/v1';

  constructor(private http: HttpClient) {}

  register(payload: registerRequest): Observable<String> {
    return this.http.post<String>(`${this.apiUrl}/register`, payload);
  }
}
