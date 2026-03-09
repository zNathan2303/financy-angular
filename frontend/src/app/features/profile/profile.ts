import { Component } from '@angular/core';
import { Header } from '../../shared/components/layout/header/header';
import { InputBase } from '../../shared/components/inputs/input-base/input-base';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { LucideAngularModule, Mail, UserRound, LogOut } from 'lucide-angular';

@Component({
  selector: 'app-profile',
  imports: [Header, InputBase, LucideAngularModule, ReactiveFormsModule],
  templateUrl: './profile.html',
  styleUrl: './profile.css',
})
export class Profile {
  readonly Mail = Mail;
  readonly UserRound = UserRound;
  readonly LogOut = LogOut;

  fullName = 'Conta teste';
  email = 'conta@teste.com';

  fullNameFormControl = new FormControl({ value: this.fullName, disabled: false });
  emailFormControl = new FormControl({ value: this.email, disabled: true });

  updateForm = new FormGroup({
    fullName: this.fullNameFormControl,
  });

  submit() {
    this.updateForm.markAllAsTouched();
    console.log(this.updateForm.value);
    console.log(this.updateForm.valid);
  }
}
