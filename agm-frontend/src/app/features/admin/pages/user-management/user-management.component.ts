import { Component } from '@angular/core';
import { AuthService } from "@core/auth/auth.service";
import { User } from "@core/models/user.model";
import { UserFormComponent } from "@features/admin/components/user-form/user-form.component";
import { UserListComponent } from "@features/admin/components/user-list/user-list.component";

@Component({
  selector: 'app-user-management',
  standalone: true,
  imports: [UserFormComponent, UserListComponent],
  templateUrl: './user-management.component.html',
})
export class UserManagementComponent {
  currentUser: User | null = null;
  selectedUser: User | undefined;

  constructor(private authService: AuthService) { }

  ngOnInit() {
    this.authService.getUser().subscribe(currentUser => {
      this.currentUser = currentUser;
    });
  }

  onUserEdit(user: User) {
    this.selectedUser = user;
  }

  onUserSaved(user: User) {
    this.selectedUser = undefined;
  }
}
