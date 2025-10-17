import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { API_URL, API_VERSION } from '../../config';

@Injectable({
  providedIn: 'root',
})
export class EngagementService {
  apiUrl = `${API_URL}${API_VERSION}/engagement`;
  private http = inject(HttpClient);

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
