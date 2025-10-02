import { Injectable } from '@angular/core';
import { API_URL, API_VERSION } from '../../config';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  apiUrl = `${API_URL}${API_VERSION}/users`
  
}
