import { AsyncPipe } from '@angular/common';
import { Component, inject } from '@angular/core';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '@core/auth/auth.service';
import { ChangePasswordRequest } from '@core/auth/models/change-password-request.model';
import { PasswordStrengthIndicatorComponent } from '@core/auth/password-strength-indicator.component';
import { User } from '@core/models/user.model';
import { AlertComponent } from '@shared/components/alert.component';
import { ButtonComponent } from '@shared/components/button.component';
import { InputComponent } from '@shared/components/input.component';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-change-password',
  standalone: true,
  imports: [
    AsyncPipe,
    AlertComponent,
    InputComponent,
    ButtonComponent,
    ReactiveFormsModule,
    PasswordStrengthIndicatorComponent,
  ],
  templateUrl: './change-password.page.html',
})
export default class ChangePasswordPage {
  private fb = inject(FormBuilder);
  private authService = inject(AuthService);
  private router = inject(Router);

  changePasswordForm: FormGroup;
  isFocused = false;
  error$: Observable<string | null>;
  user: User | null = this.authService.authResponse?.user ?? null;

  constructor() {
    this.changePasswordForm = this.fb.group({
      oldPassword: ['', [Validators.required]],
      newPassword: ['', [Validators.required, Validators.minLength(8)]],
    });
    this.error$ = this.authService.error$;
  }
  onSubmit() {
    this.changePasswordForm.markAllAsTouched();
    if (this.changePasswordForm.valid) {
      const { oldPassword, newPassword } = this.changePasswordForm.value;
      const request: ChangePasswordRequest = { oldPassword, newPassword };
      this.authService.changePassword(request).subscribe();
    }
  }

  get newPassword() {
    return this.changePasswordForm.get('newPassword') as FormControl;
  }
}
