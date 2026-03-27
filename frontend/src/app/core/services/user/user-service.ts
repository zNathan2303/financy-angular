import { HttpClient } from '@angular/common/http';
import { Injectable, signal } from '@angular/core';
import { User } from './user-model';
import { tap } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private user = signal<User | null>(null);

  constructor(private http: HttpClient) {}

  get() {
    return this.http.get<any>('/financy/v1/user').pipe(tap((res) => this.user.set(res)));
  }

  getUserData() {
    return this.user();
  }

  clearUser() {
    this.user.set(null);
  }

  getUserSignal() {
    return this.user.asReadonly();
  }
}
