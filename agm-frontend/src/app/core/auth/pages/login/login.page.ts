import {JsonPipe} from '@angular/common';
import {Component, inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators,} from '@angular/forms';
import {ButtonComponent} from '@shared/components/button.component';
import {InputComponent} from '@shared/components/input.component';
import {AuthService} from "@core/auth/auth.service";
import {StorageService} from "@core/services/storage.service";
import {Router} from "@angular/router";
import {AuthRequest} from "@core/auth/models/auth-request.model";

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule, JsonPipe, InputComponent, ButtonComponent],
  templateUrl: './login.page.html',
})
export default class LoginPage implements  OnInit {
  private fb = inject(FormBuilder);
  private authService = inject(AuthService);
  private router = inject(Router);

  loginForm!: FormGroup;
  loginResponse$ = this.authService.loginResponse$;
  error$ = this.authService.error$;

  ngOnInit(): void {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
    });
  }

  onSubmit(): void {
    if (this.loginForm.valid) {
      const authRequest: AuthRequest = this.loginForm.value;
      this.authService.login(authRequest);
      this.authService.loginResponse$.subscribe(response => {
        if (response && response.mustChangePassword) {
          this.router.navigateByUrl('/changer-mot-de-passe');
        } else if (response) {
          this.router.navigateByUrl('/accueil/preparation-assemblee/nouvelle-assemblee');
        }
      });
    }
  }
}
