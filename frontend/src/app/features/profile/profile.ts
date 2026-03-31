import { Component, computed, inject, OnInit, signal } from '@angular/core';
import { Header } from '../../shared/components/layout/header/header';
import { InputBase } from '../../shared/components/inputs/input-base/input-base';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { LucideAngularModule, Mail, UserRound, LogOut } from 'lucide-angular';
import { Auth } from '../../core/auth/services/auth';
import { Router } from '@angular/router';
import { UserService as UserServiceHttp } from '../../core/services/user/user-service';
import { UserService } from '../../shared/services/user-service';
import { LoadingService } from '../../shared/services/loading-service';
import { CustomValidators } from '../../shared/validators/custom-validators';

@Component({
  selector: 'app-profile',
  imports: [Header, InputBase, LucideAngularModule, ReactiveFormsModule],
  templateUrl: './profile.html',
  styleUrl: './profile.css',
})
export class Profile implements OnInit {
  readonly Mail = Mail;
  readonly UserRound = UserRound;
  readonly LogOut = LogOut;

  private authService = inject(Auth);
  private userService = inject(UserService);
  private userServiceHttp = inject(UserServiceHttp);
  private loadingService = inject(LoadingService);
  private router = inject(Router);

  user = this.userService.user;

  letterIcon = computed(() => {
    const name = this.user()?.name ?? '';
    return name.slice(0, 2).toUpperCase();
  });

  submitted = signal(false);

  fullNameFormControl = new FormControl(
    { value: '', disabled: false },
    { validators: [CustomValidators.trimRequired, Validators.maxLength(50)], nonNullable: true },
  );
  emailFormControl = new FormControl({ value: '', disabled: true });

  updateForm = new FormGroup({
    fullName: this.fullNameFormControl,
  });

  ngOnInit() {
    this.loadingService.show();

    if (this.user()) {
      this.fullNameFormControl.setValue(this.user()?.name ?? '');
      this.emailFormControl.setValue(this.user()?.email ?? '');

      this.loadingService.hide();
      return;
    }

    this.userServiceHttp.get().subscribe({
      next: (res) => {
        this.userService.setUser(res);

        this.fullNameFormControl.setValue(res.name);
        this.emailFormControl.setValue(res.email);
      },
      error: (err) => {
        alert('Ocorreu um erro ao carregar os dados do usuário');
        console.error(err);
        this.loadingService.hide();
      },
      complete: () => {
        this.loadingService.hide();
      },
    });
  }

  submit() {
    this.updateForm.markAllAsTouched();
    this.submitted.set(true);

    if (this.updateForm.invalid) return;

    this.loadingService.show();

    const { fullName } = this.updateForm.getRawValue();

    this.userServiceHttp.patchName(fullName).subscribe({
      next: (res) => {
        this.userService.changeName(fullName);
      },
      error: (err) => {
        alert('Ocorreu um erro ao atualizar o nome do usuário');
        console.error(err);
        this.loadingService.hide();
      },
      complete: () => {
        this.loadingService.hide();
      },
    });
  }

  logout() {
    this.authService.logout();
    this.userService.clearUser();
    this.router.navigateByUrl('/login');
  }
}
