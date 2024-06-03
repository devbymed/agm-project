import { AsyncPipe } from "@angular/common";
import { Component, OnDestroy, inject } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms";
import { ApiResponse } from "@core/models/api-response.model";
import { User } from "@core/models/user.model";
import { ToastService } from "@core/services/toast.service";
import { UserAdd } from "@features/admin/models/user-add";
import { ProfileService } from "@features/admin/services/profile.service";
import { UserService } from "@features/admin/services/user.service";
import { ButtonComponent } from "@shared/components/button/button.component";
import { InputComponent } from "@shared/components/input/input.component";
import { SelectComponent } from "@shared/components/select/select.component";
import { Subject, map, takeUntil } from "rxjs";

@Component({
  selector: 'app-user-form',
  standalone: true,
  imports: [AsyncPipe, InputComponent, SelectComponent, ButtonComponent, ReactiveFormsModule],
  templateUrl: './user-form.component.html',
  styles: ``
})
export class UserFormComponent implements OnDestroy {
  private fb = inject(FormBuilder);
  private userService = inject(UserService);
  private profileService = inject(ProfileService);
  private toastService = inject(ToastService);
  private destroy$ = new Subject<void>();

  profileOptions$ = this.profileService.getProfiles().pipe(
    map(response => {
      return response.data?.map(profile => ({ value: profile.id, label: profile.name })) || [];
    })
  );

  userForm: FormGroup;
  errorMessage: string | null = null;

  constructor() {
    this.userForm = this.fb.group({
      firstName: ['', [Validators.required, Validators.maxLength(50)]],
      lastName: ['', [Validators.required, Validators.maxLength(50)]],
      profileId: [null, Validators.required],
      email: ['', [Validators.required, Validators.email, Validators.maxLength(100)]],
    });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  addUser(): void {
    this.userForm.markAllAsTouched();
    if (this.userForm.valid) {
      const newUser = this.userForm.value as UserAdd;
      this.userService.addUser(newUser).pipe(
        takeUntil(this.destroy$)
      ).subscribe({
        next: (response: ApiResponse<User>) => {
          if (response.status === 'CREATED') {
            this.resetForm();
            this.errorMessage = null;
          }
        },
        error: (error) => {
          this.errorMessage = error.message;
        }
      });
    }
  }

  resetForm(): void {
    this.userForm.reset({
      firstName: '',
      lastName: '',
      profileId: null,
      email: '',
    });
    this.errorMessage = null;
    this.userForm.markAsPristine();
    this.userForm.markAsUntouched();
    this.userForm.updateValueAndValidity();
  }
}
