import { Component, inject, signal } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { LucideAngularModule, Mail, UserRound, LogIn } from 'lucide-angular';
import { Logo } from '../../shared/icons/logo';
import { PasswordInput } from '../../shared/components/inputs/password-input/password-input';
import { InputBase } from '../../shared/components/inputs/input-base/input-base';
import { Router } from '@angular/router';
import { Auth } from '../../core/auth/services/auth';
import { LoadingService } from '../../shared/services/loading-service';
import { UserService } from '../../core/services/user/user-service';
import { CustomValidators } from '../../shared/validators/custom-validators';

@Component({
  selector: 'app-create-account',
  imports: [Logo, PasswordInput, LucideAngularModule, ReactiveFormsModule, InputBase],
  templateUrl: './create-account.html',
  styleUrl: './create-account.css',
})
export class CreateAccount {
  readonly UserRound = UserRound;
  readonly Mail = Mail;
  readonly LogIn = LogIn;

  private authService = inject(Auth);
  private loadingService = inject(LoadingService);
  private router = inject(Router);

  submitted = signal(false);

  nameFormControl = new FormControl(
    { value: '', disabled: false },
    {
      validators: [CustomValidators.trimRequired, Validators.maxLength(50)],
      nonNullable: true,
    },
  );
  emailFormControl = new FormControl(
    { value: '', disabled: false },
    {
      validators: [CustomValidators.trimRequired, Validators.email, Validators.maxLength(300)],
      nonNullable: true,
    },
  );
  passwordFormControl = new FormControl(
    { value: '', disabled: false },
    {
      validators: [
        CustomValidators.trimRequired,
        Validators.minLength(8),
        Validators.maxLength(64),
      ],
      nonNullable: true,
    },
  );

  registerForm = new FormGroup({
    name: this.nameFormControl,
    email: this.emailFormControl,
    password: this.passwordFormControl,
  });

  submit() {
    this.registerForm.markAllAsTouched();

    this.submitted.set(true);

    if (!this.registerForm.valid) return;

    this.loadingService.show();

    const { name, email, password } = this.registerForm.getRawValue();

    const formattedName = name.trim();
    const formattedEmail = email.trim().toLowerCase();

    this.authService.register({ name: formattedName, email: formattedEmail, password }).subscribe({
      next: (res) => {
        this.router.navigateByUrl('/login');
        alert('Usuário criado com sucesso!');
      },
      error: (err) => {
        alert('Erro no cadastro');
        console.error('Erro no cadastro', err);
      },
      complete: () => {
        this.loadingService.hide();
      },
    });
  }

  goToLoginPage() {
    this.router.navigateByUrl('/login');
  }
}
