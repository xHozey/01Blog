import { Injectable } from '@angular/core';
import { API_URL, API_VERSION } from '../../config';
import { BehaviorSubject, Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  apiUrl = `${API_URL}${API_VERSION}/users`;
  private _user = new BehaviorSubject<userResponse | null>(null);
  user$: Observable<userResponse | null> = this._user.asObservable();

  constructor(private http: HttpClient) {}

  fetchCurrentUser() {
    this.http
      .get<userResponse>(`${this.apiUrl}/me`, { withCredentials: true })
      .subscribe((user) => this._user.next(user));
  }

  getUser(): userResponse | null {
    return this._user.value;
  }

  getUserById(id: number): Observable<userResponse> {
    return this.http.get<userResponse>(`${this.apiUrl}/${id}`, {
      withCredentials: true,
    });
  }

  reportUser(report: reportRequest, id: number): Observable<string> {
    return this.http.post(`${this.apiUrl}/${id}/report`, report, {
      withCredentials: true,
      responseType: 'text',
    });
  }

  updateUser() {}
}
