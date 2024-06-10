import { NgFor, NgIf } from "@angular/common";
import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { FormsModule } from "@angular/forms";
import { ApiResponse } from "@core/models/api-response.model";
import { User } from "@core/models/user.model";
import { UserService } from "@features/admin/services/user.service";
import { ButtonComponent } from "@shared/components/button/button.component";
import { ToastrService } from "ngx-toastr";
import { Subscription } from "rxjs";

@Component({
  selector: 'app-user-list',
  standalone: true,
  imports: [FormsModule, NgFor, NgIf, ButtonComponent,],
  templateUrl: './user-list.component.html',
})
export class UserListComponent implements OnInit, OnDestroy {
  users: User[] = [];
  selectedUser: User | undefined;
  private subscriptions: Subscription = new Subscription();
  @Input() currentUser: User | null = null;
  @Output() userEdit = new EventEmitter<User>();

  constructor(private userService: UserService, private toastr: ToastrService) { }

  ngOnInit() {
    const usersSubscription = this.userService.users$.subscribe(users => {
      this.users = users;
    });
    this.subscriptions.add(usersSubscription);

    this.userService.refreshUsers();
  }

  toggleSelection(user: User) {
    this.selectedUser = this.selectedUser?.id === user.id ? undefined : user;
  }

  isSelected(user: User): boolean {
    return this.selectedUser?.id === user.id;
  }

  editUser() {
    if (!this.selectedUser) {
      this.toastr.warning('Veuillez sélectionner un utilisateur à modifier.');
      return;
    }
    if (this.selectedUser.id === this.currentUser?.id) {
      this.toastr.warning('Vous ne pouvez pas modifier vos propres informations.');
      return;
    }
    this.userEdit.emit({ ...this.selectedUser });
  }

  deleteUser(id: number) {
    if (!this.selectedUser) {
      this.toastr.warning('Veuillez sélectionner un utilisateur à supprimer.');
      return;
    }
    if (this.selectedUser.id === this.currentUser?.id) {
      this.toastr.warning('Vous ne pouvez pas vous supprimer vous-même.');
      return;
    }
    const deleteUserSubscription = this.userService.deleteUser(id).subscribe((response) => {
      const apiResponse = response as ApiResponse<void>;
      this.userService.refreshUsers();
      this.toastr.success(apiResponse.message);
    });
    this.subscriptions.add(deleteUserSubscription);
  }

  ngOnDestroy() {
    this.subscriptions.unsubscribe();
  }
}
