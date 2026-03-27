import { Component, computed, inject } from '@angular/core';
import { Logo } from '../../../icons/logo';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { UserService } from '../../../../core/services/user/user-service';

@Component({
  selector: 'app-header',
  imports: [Logo, RouterLink, RouterLinkActive],
  templateUrl: './header.html',
  styleUrl: './header.css',
})
export class Header {
  private router = inject(Router);
  private userService = inject(UserService);

  currentUser = this.userService.getUserSignal();

  letterIcon = computed(() => {
    const user = this.currentUser();
    return user ? user.name.charAt(0).toUpperCase() : '.';
  });

  goToProfilePage() {
    this.router.navigateByUrl('/profile');
  }
}
