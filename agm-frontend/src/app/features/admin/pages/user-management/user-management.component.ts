import { Component } from '@angular/core';
import { UserFormComponent } from "@features/admin/components/user-form/user-form.component";
import { UserListComponent } from "@features/admin/components/user-list/user-list.component";

@Component({
  selector: 'app-user-management',
  standalone: true,
  imports: [UserFormComponent, UserListComponent],
  templateUrl: './user-management.component.html',
})
export class UserManagementComponent {

}
