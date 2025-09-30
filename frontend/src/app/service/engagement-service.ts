import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class EngagementService {
  constructor(private http: HttpClient) {}
  apiUrl = 'http://localhost:8080/api/v1/engagement';

  likePost(postId: number): Observable<string> {
    return this.http.post(`${this.apiUrl}/${postId}/post/like`, null, {
      withCredentials: true,
      responseType: 'text',
    });
  }

  likeComment(commentId: number): Observable<string> {
    return this.http.post(`${this.apiUrl}/${commentId}/comment/like`, null, {
      withCredentials: true,
      responseType: 'text',
    });
  }
}
