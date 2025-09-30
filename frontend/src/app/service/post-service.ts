import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class PostService {
  private apiUrl = 'http://localhost:8080/api/v1/posts';
  constructor(private http: HttpClient) {}
  getPosts(page: number): Observable<postResponse[]> {
    return this.http.get<postResponse[]>(`${this.apiUrl}?page=${page}`, {
      withCredentials: true,
    });
  }
  
  addPost(payload: postRequest): Observable<postResponse> {
    return this.http.post<postResponse>(`${this.apiUrl}`, payload, {
      withCredentials: true,
    });
  }
}
