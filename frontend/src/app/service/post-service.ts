import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { API_URL, API_VERSION } from '../../config';

@Injectable({
  providedIn: 'root',
})
export class PostService {
  private apiUrl = `${API_URL}${API_VERSION}/posts`;
  http = inject(HttpClient);
  
  getPosts(page: number): Observable<postResponse[]> {
    return this.http.get<postResponse[]>(`${this.apiUrl}?page=${page}`, {
      withCredentials: true,
    });
  }

  getPost(id: number): Observable<postResponse> {
    return this.http.get<postResponse>(`${this.apiUrl}/${id}`, {
      withCredentials: true,
    });
  }

  addPost(payload: postRequest): Observable<postResponse> {
    return this.http.post<postResponse>(`${this.apiUrl}`, payload, {
      withCredentials: true,
    });
  }

  deletePost(id: number): Observable<string> {
    return this.http.delete(`${this.apiUrl}/${id}`, {
      withCredentials: true,
      responseType: 'text',
    });
  }

  updatePost(payload: postRequest, id: number): Observable<postResponse> {
    return this.http.put<postResponse>(`${this.apiUrl}/${id}`, payload, { withCredentials: true });
  }

  getUserPosts(userId: number, page: number): Observable<postResponse[]> {
    return this.http.get<postResponse[]>(`${this.apiUrl}/users/${userId}?page=${page}`, {
      withCredentials: true,
    });
  }

  reportPost(payload: reportRequest, id: number): Observable<string> {
    return this.http.post(`${this.apiUrl}/${id}/report`, payload, {
      withCredentials: true,
      responseType: 'text',
    });
  }
}
