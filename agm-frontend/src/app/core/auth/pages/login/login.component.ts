import { AsyncPipe } from '@angular/common';
import { Component, OnDestroy, inject } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators
} from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '@core/auth/auth.service';
import { AuthReq } from "@core/auth/models/auth-req.model";
import { AlertComponent } from '@shared/components/alert/alert.component';
import { ButtonComponent } from '@shared/components/button/button.component';
import { InputComponent } from '@shared/components/input/input.component';
import { Subject, takeUntil } from "rxjs";

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    AsyncPipe,
    AlertComponent,
    InputComponent,
    ButtonComponent,
    ReactiveFormsModule,
  ],
  templateUrl: './login.component.html',
})
export default class LoginComponent implements OnDestroy {
  private fb = inject(FormBuilder);
  private authService = inject(AuthService);
  private router = inject(Router);
  private destroy$ = new Subject<void>();

  loginForm: FormGroup;
  errorMessage: string | null = null;

  constructor() {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required]]
    });
  }

  onSubmit(): void {
    if (this.loginForm.valid) {
      const authReq: AuthReq = this.loginForm.value as AuthReq;
      this.authService.login(authReq).pipe(
        takeUntil(this.destroy$)
      ).subscribe({
        next: (response) => {
          if (response.data) {
            if (response.data.firstLogin) {
              this.router.navigate(['/auth/changer-mot-de-passe']);
            } else {
              this.router.navigate(['/']);
            }
          }
        },
        error: (error) => {
          // this.errorMessage = 'Login failed. Please check your credentials.';
          console.error('Une erreur est survenue lors de la connexion', error);
        }
      });
    }
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
