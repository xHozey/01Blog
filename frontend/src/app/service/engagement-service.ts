import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { API_URL, API_VERSION } from '../../config';

@Injectable({
  providedIn: 'root',
})
export class EngagementService {
  constructor(private http: HttpClient) {}
  apiUrl = `${API_URL}${API_VERSION}/engagement`;

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
