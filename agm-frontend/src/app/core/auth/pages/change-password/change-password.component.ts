import { AsyncPipe } from '@angular/common';
import { Component } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators
} from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '@core/auth/auth.service';
import { AlertComponent } from '@shared/components/alert/alert.component';
import { ButtonComponent } from '@shared/components/button/button.component';
import { InputComponent } from '@shared/components/input/input.component';

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
export default class ChangePasswordComponent {
  changePasswordForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.changePasswordForm = this.fb.group({
      oldPassword: ['', Validators.required],
      newPassword: ['', [Validators.required, Validators.minLength(8)]]
    });
  }

  // onSubmit() {
  //   if (this.changePasswordForm.valid) {
  //     const formValue = this.changePasswordForm.value;
  //     const changePasswordRequest: ChangePasswordRequest = {
  //       oldPassword: formValue.oldPassword,
  //       newPassword: formValue.newPassword
  //     };
  //     this.authService.changePassword(changePasswordRequest).subscribe({
  //       next: (response) => {
  //         if (response.status === 'OK') {
  //           this.router.navigate(['/accueil']);
  //         } else {
  //           console.error('Password change failed: ', response.message);
  //         }
  //       },
  //       error: (error) => {
  //         console.error('Password change failed', error);
  //       }
  //     });
  //   }
  // }
}
