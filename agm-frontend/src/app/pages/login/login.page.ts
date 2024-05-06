import { Component, inject } from '@angular/core';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { ButtonComponent } from '../../shared/components/button.component';
import { InputComponent } from '../../shared/components/input.component';
import { JsonPipe } from '@angular/common';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule, JsonPipe, InputComponent, ButtonComponent],
  templateUrl: './login.page.html',
})
export default class LoginPage {
  private authService = inject(AuthService);
  private router = inject(Router);

  loginForm!: FormGroup;

  constructor(private fb: FormBuilder) {
    this.loginForm = this.fb.group({
      email: new FormControl('', [Validators.required, Validators.email]),
      password: new FormControl('', [Validators.required]),
    });
  }

  // onSubmit() {
  //   if (this.loginForm.valid) {
  //     this.authService.login(this.loginForm.value).subscribe({
  //       next: (value) => {
  //         if (value.data?.mustChangePassword) {
  //           this.router.navigate(['/change-password']);
  //         } else {
  //           this.router.navigate(['/user-management']);
  //         }
  //       },
  //     });
  //   } else {
  //     this.loginForm.markAllAsTouched();
  //   }
  // }
}
