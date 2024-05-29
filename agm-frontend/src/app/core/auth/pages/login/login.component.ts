import { AsyncPipe } from '@angular/common';
import { Component, OnDestroy, OnInit, inject } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators
} from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '@core/auth/auth.service';
import { AuthRequest } from "@core/auth/models/auth-request.model";
import { AuthResponse } from "@core/auth/models/auth-response.model";
import { ApiResponse } from "@core/models/api-response.model";
import { AlertComponent } from '@shared/components/alert/alert.component';
import { ButtonComponent } from '@shared/components/button/button.component';
import { InputComponent } from '@shared/components/input/input.component';
import { BehaviorSubject, Subject, takeUntil } from "rxjs";

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
export default class LoginComponent implements OnInit, OnDestroy {
  private fb = inject(FormBuilder);
  private authService = inject(AuthService);
  private router = inject(Router);

  loginForm: FormGroup;
  errorMessage$ = new BehaviorSubject<string | null>(null);
  private destroy$ = new Subject<void>();

  ngOnInit(): void {
    this.initializeForm();
  }

  private initializeForm(): void {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  onSubmit(): void {
    if (this.loginForm.valid) {
      const authRequest: AuthRequest = this.loginForm.value;
      this.authService.login(authRequest).pipe(
        takeUntil(this.destroy$)
      ).subscribe({
        next: response => this.handleLoginResponse(response),
        error: error => this.handleError(error)
      });
    }
  }

  private handleLoginResponse(response: ApiResponse<AuthResponse>): void {
    if (response.status === 'OK' && response.data) {
      if (response.data.firstLogin) {
        this.router.navigate(['/auth/changer-mot-de-passe']);
      } else {
        this.router.navigate(['/accueil']);
      }
    } else {
      this.errorMessage$.next(response.message || 'Une erreur est survenue');
    }
  }

  private handleError(error: any): void {
    if (error.error && error.error.message) {
      this.errorMessage$.next(error.error.message);
    } else {
      this.errorMessage$.next('Une erreur est survenue. Veuillez réessayer plus tard.');
    }
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  get errorMessage() {
    return this.errorMessage$.asObservable();
  }
}
