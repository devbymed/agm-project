import { Component } from '@angular/core';
import { UserFormComponent } from "@features/admin/components/user-form/user-form.component";
import { ButtonComponent } from "@shared/components/button/button.component";

@Component({
  selector: 'app-user-management',
  standalone: true,
  imports: [UserFormComponent, ButtonComponent],
  templateUrl: './users.component.html',
})
export class UsersComponent { }
