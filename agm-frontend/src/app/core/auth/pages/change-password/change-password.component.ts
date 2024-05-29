import { AsyncPipe } from '@angular/common';
import { Component, OnDestroy } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators
} from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '@core/auth/auth.service';
import { ChangePwdReq } from "@core/auth/models/change-pwd-req";
import { AlertComponent } from '@shared/components/alert/alert.component';
import { ButtonComponent } from '@shared/components/button/button.component';
import { InputComponent } from '@shared/components/input/input.component';
import { Subject, takeUntil } from "rxjs";

@Component({
  selector: 'app-change-password',
  standalone: true,
  imports: [
    AsyncPipe,
    AlertComponent,
    InputComponent,
    ButtonComponent,
    ReactiveFormsModule,
  ],
  templateUrl: './change-password.component.html',
})
export default class ChangePasswordComponent implements OnDestroy {
  changePasswordForm: FormGroup;
  private destroy$ = new Subject<void>();

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private authService: AuthService,
  ) {
    this.changePasswordForm = this.fb.group({
      oldPassword: ['', Validators.required],
      newPassword: ['', [Validators.required, Validators.minLength(8)]]
    });
  }

  onSubmit(): void {
    if (this.changePasswordForm.invalid) {
      console.log('Form is invalid');
      return;
    }

    const changePwdReq: ChangePwdReq = this.changePasswordForm.value;
    this.authService.changePassword(changePwdReq)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (response) => {
          if (response.status === 'OK') {
            console.log('Password changed successfully, navigating to home');
            this.router.navigate(['/accueil']);
          }
        },
        error: (err) => {
          console.error('Une erreur est survenue lors du changement de mot de passe', err);
        }
      });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
