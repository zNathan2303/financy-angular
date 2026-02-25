import { NgClass } from '@angular/common';
import { Component, input, signal } from '@angular/core';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { LucideAngularModule, EyeClosed, Eye, Lock } from 'lucide-angular';

@Component({
  selector: 'password-input',
  imports: [ReactiveFormsModule, NgClass, LucideAngularModule],
  templateUrl: './password-input.html',
  styleUrl: './password-input.css',
})
export class PasswordInput {
  readonly EyeClosed = EyeClosed;
  readonly Eye = Eye;
  readonly Lock = Lock;

  formControlPassword = input.required<FormControl>();

  isFocused = signal(false);
  isInvalid = signal(false);
  isHidden = signal(true);

  getStateClasses() {
    return {
      error: this.isInvalid(),
      focused: this.isFocused(),
      'contains-text': this.formControlPassword().value != '',
    };
  }

  gainFocus() {
    this.isInvalid.set(false);
    this.isFocused.set(true);
  }

  showPassword() {
    this.isHidden.set(!this.isHidden());
  }
}
