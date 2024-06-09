import { AsyncPipe } from "@angular/common";
import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms";
import { User } from "@core/models/user.model";
import { ProfileService } from "@features/admin/services/profile.service";
import { UserService } from "@features/admin/services/user.service";
import { AlertComponent } from "@shared/components/alert/alert.component";
import { ButtonComponent } from "@shared/components/button/button.component";
import { InputComponent } from "@shared/components/form/input/input.component";
import { SelectComponent } from "@shared/components/form/select/select.component";
import { ToastrService } from "ngx-toastr";
import { Observable, Subject, map, takeUntil } from "rxjs";

@Component({
  selector: 'app-user-form',
  standalone: true,
  imports: [AsyncPipe, AlertComponent, InputComponent, SelectComponent, ButtonComponent, ReactiveFormsModule],
  templateUrl: './user-form.component.html',
})
export class UserFormComponent implements OnInit, OnDestroy {
  userForm: FormGroup;
  isEditing: boolean;
  errorMessage: string | null = null;
  profileOptions$: Observable<{ value: string, label: string }[]>;
  private destroy$ = new Subject<void>();

  constructor(
    private formBuilder: FormBuilder,
    private userService: UserService,
    private profileService: ProfileService,
    private toastr: ToastrService
  ) {
    this.profileOptions$ = this.profileService.getProfiles().pipe(
      map(profiles => profiles.map(profile => ({ value: profile.id.toString(), label: profile.name })))
    );
  }

  ngOnInit() {
    this.initForm();
    this.listenToSelectedUser();
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }

  initForm() {
    this.userForm = this.formBuilder.group({
      id: [''],
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      profileId: ['', Validators.required],
    });
  }

  private listenToSelectedUser() {
    this.userService.selectedUser$
      .pipe(takeUntil(this.destroy$))
      .subscribe(user => {
        if (user) {
          this.isEditing = true;
          this.userForm.patchValue({
            id: user.id,
            firstName: user.firstName,
            lastName: user.lastName,
            email: user.email,
            profileId: user.profile.id.toString(),
          });
        } else {
          this.isEditing = false;
        }
      });
  }

  submitForm() {
    this.userForm.markAsUntouched();
    if (this.userForm.valid) {
      const user = this.userForm.value as User;
      if (this.isEditing) {
        this.updateUser(user);
      } else {
        this.addUser(user);
      }
    }
  }

  updateUser(user: User) {
    this.userService.updateUser(user).pipe(
      takeUntil(this.destroy$)
    ).subscribe({
      next: (response) => {
        if (response?.status === 'OK') {
          this.toastr.success(response.message);
          this.resetForm();
          this.isEditing = false;
        }
      },
      error: (e) => {
        this.errorMessage = e.error.message;
        console.error(e.error.message);
      }
    });
  }

  addUser(user: User) {
    this.userService.addUser(user).pipe(
      takeUntil(this.destroy$)
    ).subscribe({
      next: (response) => {
        if (response?.status === 'CREATED') {
          this.toastr.success(response.message);
          this.resetForm();
        }
      },
      error: (e) => {
        this.errorMessage = e.error.message;
      }
    });
  }

  resetForm() {
    this.userForm.reset(
      {
        firstName: '',
        lastName: '',
        email: '',
        profileId: '',
      },
    );
    this.isEditing = false;
    this.errorMessage = null;
  }
}
