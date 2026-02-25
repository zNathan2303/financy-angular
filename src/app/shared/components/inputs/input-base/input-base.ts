import { NgClass } from '@angular/common';
import { Component, input, signal } from '@angular/core';
import { FormControl, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'input-base',
  imports: [ReactiveFormsModule, NgClass],
  templateUrl: './input-base.html',
  styleUrl: './input-base.css',
})
export class InputBase {
  formControlInput = input.required<FormControl>();
  labelText = input.required<string>();
  fieldName = input.required<string>();
  placeholder = input.required<string>();
  type = input<'email' | 'text'>('text');

  isFocused = signal(false);
  isInvalid = signal(false);

  getStateClasses() {
    return {
      error: this.isInvalid(),
      focused: this.isFocused(),
      'contains-text': this.formControlInput().value != '',
    };
  }

  gainFocus() {
    this.isInvalid.set(false);
    this.isFocused.set(true);
  }
}
