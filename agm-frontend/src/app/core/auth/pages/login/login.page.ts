import { AsyncPipe } from '@angular/common';
import { Component, OnDestroy, OnInit, inject } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '@core/auth/auth.service';
import { AuthRequest } from '@core/auth/models/auth-request.model';
import { AlertComponent } from '@shared/components/alert.component';
import { ButtonComponent } from '@shared/components/button.component';
import { InputComponent } from '@shared/components/input.component';
import { Subject, takeUntil, tap } from 'rxjs';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    AsyncPipe,
    ButtonComponent,
    AlertComponent,
    InputComponent,
    ReactiveFormsModule,
  ],
  templateUrl: './login.page.html',
})
export default class LoginPage implements OnInit, OnDestroy {
  private fb = inject(FormBuilder);
  private authService = inject(AuthService);
  private router = inject(Router);
  private destroy$ = new Subject<void>();

  loginForm!: FormGroup;
  loginResponse$ = this.authService.loginResponse$;
  error$ = this.authService.error$;

  ngOnInit(): void {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
    });

    this.loginResponse$
      .pipe(
        tap((response) => {
          if (response) {
            if (response.mustChangePassword) {
              this.router.navigateByUrl('/auth/changer-mot-de-passe');
            } else {
              this.router.navigateByUrl(
                '/accueil/preparation-assemblee/nouvelle-assemblee',
              );
            }
          }
        }),
        takeUntil(this.destroy$),
      )
      .subscribe();
  }

  onSubmit(): void {
    if (this.loginForm.valid) {
      const authRequest: AuthRequest = this.loginForm.value;
      this.authService.login(authRequest);
    } else {
      this.loginForm.markAllAsTouched();
    }
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
