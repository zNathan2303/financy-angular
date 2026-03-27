import { Component, inject, signal } from '@angular/core';
import { Logo } from '../../shared/icons/logo';
import { PasswordInput } from '../../shared/components/inputs/password-input/password-input';
import { LucideAngularModule, Mail, UserRoundPlus } from 'lucide-angular';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { InputBase } from '../../shared/components/inputs/input-base/input-base';
import { Auth } from '../../core/auth/services/auth';
import { Router, RouterLink } from '@angular/router';
import { LoadingService } from '../../shared/services/loading-service';
import { UserService } from '../../core/services/user/user-service';
import { forkJoin, switchMap } from 'rxjs';

@Component({
  selector: 'app-login-page',
  imports: [Logo, PasswordInput, LucideAngularModule, ReactiveFormsModule, InputBase, RouterLink],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {
  readonly UserRoundPlus = UserRoundPlus;
  readonly Mail = Mail;

  private authService = inject(Auth);
  private loadingService = inject(LoadingService);
  private userService = inject(UserService);
  private router = inject(Router);

  submitted = signal(false);

  emailFormControl = new FormControl(
    { value: '', disabled: false },
    {
      validators: [Validators.required, Validators.email, Validators.maxLength(300)],
      nonNullable: true,
    },
  );
  passwordFormControl = new FormControl(
    { value: '', disabled: false },
    {
      validators: [Validators.required, Validators.minLength(8), Validators.maxLength(64)],
      nonNullable: true,
    },
  );
  rememberMeFormControl = new FormControl(
    { value: false, disabled: false },
    {
      nonNullable: true,
    },
  );

  loginForm = new FormGroup({
    email: this.emailFormControl,
    password: this.passwordFormControl,
    rememberMe: this.rememberMeFormControl,
  });

  submit() {
    this.loginForm.markAllAsTouched();

    this.submitted.set(true);

    if (!this.loginForm.valid) return;

    this.loadingService.show();

    const { email, password, rememberMe } = this.loginForm.getRawValue();

    const formattedEmail = email.trim().toLowerCase();

    this.authService
      .login({ email: formattedEmail, password }, rememberMe)
      .pipe(switchMap(() => this.userService.get()))
      .subscribe({
        next: (res) => {
          this.router.navigateByUrl('/dashboard');
        },
        error: (err) => {
          alert('Erro no login');
          console.error('Erro no login', err);
        },
        complete: () => {
          this.loadingService.hide();
        },
      });
  }
  goToCreateAccountPage() {
    this.router.navigateByUrl('/create-account');
  }
}
