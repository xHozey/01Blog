import { Injectable } from '@angular/core';
import { API_URL, API_VERSION } from '../../config';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { commentResponse } from '../models/commentResponse';

@Injectable({
  providedIn: 'root',
})
export class CommentService {
  apiUrl = `${API_URL}${API_VERSION}/comments`;
  constructor(private http: HttpClient) {}

  fetchComments(postId: number, page: number): Observable<commentResponse[]> {
    return this.http.get<commentResponse[]>(`${this.apiUrl}/${postId}?page=${page}`, {
      withCredentials: true,
    });
  }

  addComment(payload: FormData): Observable<commentResponse> {
    return this.http.post<commentResponse>(`${this.apiUrl}`, payload, { withCredentials: true });
  }

  updateComment(payload: FormData): Observable<commentResponse> {
    return this.http.put<commentResponse>(`${this.apiUrl}`, payload, { withCredentials: true });
  }

  deleteComment(id: number): Observable<string> {
    return this.http.delete(`${this.apiUrl}/${id}`, {
      withCredentials: true,
      responseType: 'text',
    });
  }
}
