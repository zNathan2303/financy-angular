import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthRequest, AuthResponse } from '../models/auth';
import { tap } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class Auth {
  private readonly apiUrl = 'https://financy-angular-java.onrender.com';

  constructor(private http: HttpClient) {}

  login({ email, password }: AuthRequest) {
    return this.http
      .post<AuthResponse>(`${this.apiUrl}/financy/v1/auth/login`, { email, password })
      .pipe(
        tap((res) => {
          localStorage.setItem('token', res.token);
        }),
      );
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  logout() {
    localStorage.removeItem('token');
  }

  isAuthenticated(): boolean {
    return !!this.getToken();
  }
}
