import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { tap } from 'rxjs';
import { User } from './user-model';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  constructor(private http: HttpClient) {}

  private readonly pathUser = '/financy/v1/user';

  get() {
    return this.http.get<User>(this.pathUser).pipe(
      tap((res) => {
        localStorage.setItem('user-info', JSON.stringify(res));
      }),
    );
  }

  patchName(name: string) {
    return this.http.patch(`${this.pathUser}/name`, { name });
  }
}
