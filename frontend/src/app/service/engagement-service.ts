import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class EngagementService {
  constructor(private http: HttpClient) {}
  apiUrl = "http://localhost:8080"

  
}
