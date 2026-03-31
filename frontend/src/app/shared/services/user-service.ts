import { Injectable, signal } from '@angular/core';
import { User } from '../../core/services/user/user-model';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private userSignal = signal<User | null>(this.readUser());

  user = this.userSignal.asReadonly();

  private readUser(): User | null {
    const userInString = localStorage.getItem('user-info');
    if (!userInString) return null;

    return JSON.parse(userInString) as User;
  }

  setUser(user: User) {
    localStorage.setItem('user-info', JSON.stringify(user));
    this.userSignal.set(user);
  }

  changeName(name: string) {
    const currentUser = this.userSignal();
    if (!currentUser) return;

    const updatedUser = { ...currentUser, name };
    this.setUser(updatedUser);
  }

  clearUser() {
    localStorage.removeItem('user-info');
    this.userSignal.set(null);
  }
}
