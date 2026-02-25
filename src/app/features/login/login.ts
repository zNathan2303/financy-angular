import { Component } from '@angular/core';
import { Logo } from '../../shared/components/icons/logo';
import { PasswordInput } from '../../shared/components/inputs/password-input/password-input';
import { LucideAngularModule, Mail, UserRoundPlus } from 'lucide-angular';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { InputBase } from '../../shared/components/inputs/input-base/input-base';

@Component({
  selector: 'login-page',
  imports: [Logo, PasswordInput, LucideAngularModule, ReactiveFormsModule, InputBase],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {
  readonly UserRoundPlus = UserRoundPlus;
  readonly Mail = Mail;

  emailFormControl = new FormControl({ value: '', disabled: false }, [
    Validators.required,
    Validators.email,
  ]);
  passwordFormControl = new FormControl({ value: '', disabled: false }, [
    Validators.required,
    Validators.minLength(8),
  ]);

  loginForm = new FormGroup({
    email: this.emailFormControl,
    password: this.passwordFormControl,
  });

  submit() {
    this.loginForm.markAllAsTouched();
    console.log(this.loginForm.value);
    console.log(this.loginForm.valid);
  }
}
