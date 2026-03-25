import { Component, inject } from '@angular/core';
import { Logo } from '../../../icons/logo';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';

@Component({
  selector: 'app-header',
  imports: [Logo, RouterLink, RouterLinkActive],
  templateUrl: './header.html',
  styleUrl: './header.css',
})
export class Header {
  private router = inject(Router);

  goToProfilePage() {
    this.router.navigateByUrl('/profile');
  }
}
