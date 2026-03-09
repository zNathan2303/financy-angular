import { Component } from '@angular/core';
import { Logo } from '../../../icons/logo';

@Component({
  selector: 'app-header',
  imports: [Logo],
  templateUrl: './header.html',
  styleUrl: './header.css',
})
export class Header {}
