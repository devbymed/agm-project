import { AsyncPipe, NgFor } from "@angular/common";
import { Component, OnInit } from '@angular/core';
import { FormsModule } from "@angular/forms";
import { UserService } from "@features/admin/services/user.service";
import { ButtonComponent } from "@shared/components/button/button.component";

@Component({
  selector: 'app-user-list',
  standalone: true,
  imports: [FormsModule, ButtonComponent, NgFor, AsyncPipe],
  templateUrl: './user-list.component.html',
})
export class UserListComponent implements OnInit {
  users$ = this.userService.users$;
  errorMessage: string | null = null;

  constructor(private userService: UserService) { }

  ngOnInit() {
    this.userService.fetchUsers();
  }

  onEdit() {
    const user = this.userService.getCheckedUser();
    this.userService.selectedUser(user);
  }
}
