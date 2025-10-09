import { inject, Injectable } from '@angular/core';
import { API_URL, API_VERSION } from '../../config';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class MediaService {
  private apiUrl = `${API_URL}${API_VERSION}/cloud`;
  private http: HttpClient = inject(HttpClient);

  addPostImage(form: FormData): Observable<string> {
    return this.http.post(`${this.apiUrl}/post/image`, form, {
      withCredentials: true,
      responseType: 'text',
    });
  }

  addPostVideo(form: FormData): Observable<string> {
    return this.http.post(`${this.apiUrl}/post/video`, form, {
      withCredentials: true,
      responseType: 'text',
    });
  }

  addUserIcon(form: FormData): Observable<string> {
    return this.http.post(`${this.apiUrl}/user/icon`, form, {
      withCredentials: true,
      responseType: 'text',
    });
  }
}
