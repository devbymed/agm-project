import { AsyncPipe } from "@angular/common";
import { Component, OnDestroy, inject } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms";
import { ApiResponse } from "@core/models/api-response.model";
import { User } from "@core/models/user.model";
import { UserAdd } from "@features/admin/models/user-add";
import { ProfileService } from "@features/admin/services/profile.service";
import { UserService } from "@features/admin/services/user.service";
import { AlertComponent } from "@shared/components/alert/alert.component";
import { ButtonComponent } from "@shared/components/button/button.component";
import { InputComponent } from "@shared/components/form/input/input.component";
import { SelectComponent } from "@shared/components/form/select/select.component";
import { ToastrService } from "ngx-toastr";
import { Subject, map, takeUntil } from "rxjs";

@Component({
  selector: 'app-user-form',
  standalone: true,
  imports: [AsyncPipe, AlertComponent, InputComponent, SelectComponent, ButtonComponent, ReactiveFormsModule],
  templateUrl: './user-form.component.html',
})
export class UserFormComponent implements OnDestroy {

  private fb = inject(FormBuilder);
  private userService = inject(UserService);
  private profileService = inject(ProfileService);
  private toastr = inject(ToastrService);
  private destroy$ = new Subject<void>();

  userForm: FormGroup;
  selectedUser: User | null = null;
  errorMessage: string | null = null;

  profileOptions$ = this.profileService.getProfiles().pipe(
    map(response => {
      return response.data?.map(profile => ({ value: profile.id, label: profile.name })) || [];
    })
  );

  constructor() {
    this.userForm = this.fb.group({
      firstName: ['', [Validators.required]],
      lastName: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      profileId: ['', [Validators.required]],
    });
  }

  ngOnInit() {
    this.userService.selectedUser$.subscribe(user => {
      if (user) {
        this.selectedUser = user;
        this.userForm.patchValue({
          firstName: user.firstName,
          lastName: user.lastName,
          email: user.email,
          profileId: user.profile.id,
        });
      }
    });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  addUser() {
    this.userForm.markAllAsTouched();
    if (this.userForm.valid) {
      const newUser = this.userForm.value as UserAdd;
      this.userService.addUser(newUser).pipe(
        takeUntil(this.destroy$)
      ).subscribe({
        next: (response: ApiResponse<User>) => {
          if (response.status === 'CREATED') {
            this.errorMessage = null;
            this.toastr.success(response.message);
            this.resetForm();
          }
        },
        error: (e) => {
          this.errorMessage = e.error.message;
        }
      });
    }
  }

  resetForm() {
    this.userForm.reset(
      { firstName: '', lastName: '', profileId: '', email: '' },
    );
    this.errorMessage = null;
  }
}
