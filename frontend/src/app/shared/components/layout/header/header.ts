import { Component, computed, inject, OnInit, signal } from '@angular/core';
import { Logo } from '../../../icons/logo';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { UserService as UserServiceHttp } from '../../../../core/services/user/user-service';
import { UserService } from '../../../services/user-service';
import { User } from '../../../../core/services/user/user-model';

@Component({
  selector: 'app-header',
  imports: [Logo, RouterLink, RouterLinkActive],
  templateUrl: './header.html',
  styleUrl: './header.css',
})
export class Header {
  private router = inject(Router);
  private userService = inject(UserService);
  private userServiceHttp = inject(UserServiceHttp);

  currentUserInfo = this.userService.user;

  letterIcon = computed(() => {
    const name = this.currentUserInfo()?.name ?? '';
    return name.slice(0, 2).toUpperCase();
  });

  constructor() {
    if (!this.currentUserInfo()) {
      this.userServiceHttp.get().subscribe((res) => {
        this.userService.setUser(res);
      });
    }
  }

  goToProfilePage() {
    this.router.navigateByUrl('/profile');
  }
}
