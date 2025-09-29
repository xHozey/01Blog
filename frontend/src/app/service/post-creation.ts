import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class PostCreation {
  private apiUrl = 'http://localhost:8080/api/v1';

  constructor(private http: HttpClient) {}
}
