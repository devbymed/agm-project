import { Component, OnInit, inject } from '@angular/core';
import { ApiResponse } from "@core/models/api-response.model";
import { User } from "@core/models/user.model";
import { UserFormComponent } from "@features/admin/components/user-form/user-form.component";
import { UserService } from "@features/admin/services/user.service";
import { ButtonComponent } from "@shared/components/button/button.component";
import { Subject, takeUntil } from "rxjs";

@Component({
  selector: 'app-user-management',
  standalone: true,
  imports: [UserFormComponent, ButtonComponent],
  templateUrl: './users.component.html',
})
export class UsersComponent implements OnInit {
  private userService = inject(UserService);
  private destroy$ = new Subject<void>();
  errorMessage: string | null = null;
  users: User[] = [];

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
}
