import { Component, inject } from '@angular/core';
import { Logo } from '../../shared/icons/logo';
import { PasswordInput } from '../../shared/components/inputs/password-input/password-input';
import { LucideAngularModule, Mail, UserRoundPlus } from 'lucide-angular';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { InputBase } from '../../shared/components/inputs/input-base/input-base';
import { Auth } from '../../core/auth/services/auth';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login-page',
  imports: [Logo, PasswordInput, LucideAngularModule, ReactiveFormsModule, InputBase],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {
  readonly UserRoundPlus = UserRoundPlus;
  readonly Mail = Mail;

  private authService = inject(Auth);
  private router = inject(Router);

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

  loginForm = new FormGroup({
    email: this.emailFormControl,
    password: this.passwordFormControl,
  });

  submit() {
    this.loginForm.markAllAsTouched();

    if (this.loginForm.valid) {
      const { email, password } = this.loginForm.getRawValue();

      if (password !== password.trim()) {
        alert('Senha não pode começar ou terminar com espaço');
        return;
      }

      const formattedEmail = email.trim().toLowerCase();

      this.authService.login({ email: formattedEmail, password }).subscribe({
        next: (response) => {
          this.router.navigateByUrl('/dashboard');
        },
        error: (err) => {
          alert('Erro no login');
          console.error('Erro no login', err);
        },
      });
    } else {
      alert('E-mail ou senha inválidos');
    }
  }
}
