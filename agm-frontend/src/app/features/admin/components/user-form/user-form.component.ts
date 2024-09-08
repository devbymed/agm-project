import { NgIf } from '@angular/common';
import {
  Component,
  EventEmitter,
  Input,
  OnChanges,
  OnDestroy,
  Output,
  SimpleChanges,
} from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { ApiResponse } from '@core/models/api-response.model';
import { Profile } from '@core/models/profile.model';
import { User } from '@core/models/user.model';
import { ProfileService } from '@features/admin/services/profile.service';
import { UserService } from '@features/admin/services/user.service';
import { AlertComponent } from '@shared/components/alert/alert.component';
import { ButtonComponent } from '@shared/components/button/button.component';
import { InputComponent } from '@shared/components/form/input/input.component';
import { SelectComponent } from '@shared/components/form/select/select.component';
import { ToastrService } from 'ngx-toastr';
import { Observable, Subscription, catchError, throwError } from 'rxjs';

interface SelectOption {
  value: string;
  label: string;
}

@Component({
  selector: 'app-user-form',
  standalone: true,
  imports: [
    NgIf,
    ReactiveFormsModule,
    AlertComponent,
    InputComponent,
    SelectComponent,
    ButtonComponent,
  ],
  templateUrl: './user-form.component.html',
})
export class UserFormComponent implements OnChanges, OnDestroy {
  @Input() user: User | undefined;
  @Output() userSaved = new EventEmitter<User>();

  userForm: FormGroup;
  options: SelectOption[] = [];
  private subscriptions: Subscription = new Subscription();
  formTitle: string = 'Ajouter un utilisateur';
  errorMessage: string | null = null;
  isEditMode: boolean = false;

  constructor(
    private fb: FormBuilder,
    private userService: UserService,
    private profileService: ProfileService,
    private toastr: ToastrService,
  ) {
    this.userForm = this.fb.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      profileId: ['', Validators.required],
    });

    const profileSubscription = this.profileService
      .getProfiles()
      .subscribe((response) => {
        const apiResponse = response as ApiResponse<Profile[]>;
        this.options = (apiResponse.data || []).map((profile) => ({
          value: profile.id.toString(),
          label: profile.name,
        }));
      });
    this.subscriptions.add(profileSubscription);
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['user'] && changes['user'].currentValue) {
      this.userForm.patchValue({
        firstName: this.user?.firstName,
        lastName: this.user?.lastName,
        email: this.user?.email,
        profileId: this.user?.profile.id.toString(),
      });
      this.userForm.get('firstName')?.disable();
      this.userForm.get('lastName')?.disable();
      this.isEditMode = true;
      this.formTitle = 'Modifier utilisateur';
    } else {
      this.resetForm();
    }
  }

  onSubmit() {
    this.userForm.markAllAsTouched();
    if (this.userForm.valid) {
      const observable =
        this.user && this.user.id
          ? this.userService.updateUser(this.user.id, this.userForm.value)
          : this.userService.addUser(this.userForm.value);

      this.handleApiResponse(observable);
    }
  }

  private handleApiResponse(observable: Observable<ApiResponse<User>>) {
    const subscription = observable
      .pipe(
        catchError((error) => {
          this.errorMessage = error.error.message;
          return throwError(() => error);
        }),
      )
      .subscribe((response) => {
        const apiResponse = response as ApiResponse<User>;
        this.userSaved.emit(apiResponse.data!);
        this.toastr.success(apiResponse.message);
        this.errorMessage = null;
        this.resetForm();
      });
    this.subscriptions.add(subscription);
  }

  resetForm() {
    this.userForm.reset({
      firstName: '',
      lastName: '',
      email: '',
      profileId: '',
    });
    this.userForm.get('firstName')?.enable();
    this.userForm.get('lastName')?.enable();
    this.isEditMode = false;
    this.errorMessage = null;
    this.formTitle = 'Ajouter un utilisateur';
  }

  ngOnDestroy() {
    this.subscriptions.unsubscribe();
  }
}
