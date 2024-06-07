import { Component, inject } from '@angular/core';
import { FormsModule } from "@angular/forms";
import { ApiResponse } from "@core/models/api-response.model";
import { User } from "@core/models/user.model";
import { UserService } from "@features/admin/services/user.service";
import { ButtonComponent } from "@shared/components/button/button.component";
import { ToastrService } from "ngx-toastr";
import { Subject, takeUntil } from "rxjs";

@Component({
  selector: 'app-user-list',
  standalone: true,
  imports: [FormsModule, ButtonComponent],
  templateUrl: './user-list.component.html',
})
export class UserListComponent {
  private userService = inject(UserService);
  private toastrService = inject(ToastrService);
  private destroy$ = new Subject<void>();
  errorMessage: string | null = null;
  users: User[] = [];
  selectAll = false;
  selectedUser: User | null = null;

  ngOnInit(): void {
    this.loadUsers();
  }

  private loadUsers(): void {
    this.userService.getUsers().pipe(
      takeUntil(this.destroy$)
    ).subscribe({
      next: (response: ApiResponse<User[]>) => {
        if (response.status === 'OK' && response.data) {
          this.users = response.data;
        } else {
          this.errorMessage = 'Erreur lors du chargement des utilisateurs';
        }
      },
      error: (error) => {
        console.error('Impossible de charger les utilisateurs', error);
        this.errorMessage = 'Impossible de charger les utilisateurs';
      }
    });
  }

  loadUser() {
    const selectedUsers = this.users.filter(user => user.checked);
    if (selectedUsers.length !== 1) {
      this.toastrService.warning('Veuillez sélectionner un seul utilisateur');
      return;
    }

    const selectedUser = selectedUsers[0];
    this.userService.selectUser(selectedUser);
  }

  checkAll() {
    for (let user of this.users) {
      user.checked = this.selectAll;
    }
  }

  check() {
    this.selectAll = this.users.every(user => user.checked);
  }
}
