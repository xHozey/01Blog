import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { API_URL, API_VERSION } from '../../config';

@Injectable({
  providedIn: 'root',
})
export class PostService {
  private apiUrl = `${API_URL}${API_VERSION}/posts`;
  constructor(private http: HttpClient) {}

  getPosts(page: number): Observable<postResponse[]> {
    return this.http.get<postResponse[]>(`${this.apiUrl}?page=${page}`, {
      withCredentials: true,
    });
  }

  addPost(formData: FormData): Observable<postResponse> {
    return this.http.post<postResponse>(`${this.apiUrl}`, formData, {
      withCredentials: true,
    });
  }

  deletePost(id: number): Observable<string> {
    return this.http.delete(`${this.apiUrl}/${id}`, {
      withCredentials: true,
      responseType: 'text',
    });
  }

  updatePost(formData: FormData): Observable<postResponse> {
    return this.http.put<postResponse>(`${this.apiUrl}`, formData, { withCredentials: true });
  }

  reportPost(payload: reportRequest): Observable<string> {
    return this.http.post(`${this.apiUrl}/report`, payload, {
      withCredentials: true,
      responseType: 'text',
    });
  }

  hidePost(id: number): Observable<string> {
    return this.http.post(`${this.apiUrl}/hide/${id}`, null, {
      withCredentials: true,
      responseType: 'text',
    });
  }
}
