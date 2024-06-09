import { HttpClient } from "@angular/common/http";
import { Injectable, inject } from '@angular/core';
import { ApiResponse } from "@core/models/api-response.model";
import { User } from "@core/models/user.model";
import { environment } from "@env/environment";
import { BehaviorSubject, Observable, catchError, map, of } from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private http = inject(HttpClient);
  private apiUrl = environment.apiUrl;

  private readonly usersSubject = new BehaviorSubject<User[]>([]);
  private readonly selectedUserSubject = new BehaviorSubject<User | null>(null);

  readonly users$ = this.usersSubject.asObservable();
  readonly selectedUser$ = this.selectedUserSubject.asObservable();

  selectedUser(user: User | null): void {
    this.selectedUserSubject.next(user);
  }

  getCheckedUser(): User | null {
    return this.usersSubject.value.find(user => user.checked) || null;
  }

  fetchUsers(): void {
    this.http.get<ApiResponse<User[]>>(`${this.apiUrl}/users`).pipe(
      map(response => response.data ? response.data.map(user => ({ ...user, checked: false })) : []),
      catchError(error => {
        console.error('Failed to fetch users', error);
        return of([]);
      })
    ).subscribe(users => this.usersSubject.next(users)); // Update the subject with new data
  }

  addUser(user: User): Observable<ApiResponse<any> | null> {
    return this.http.post<ApiResponse<any>>(`${this.apiUrl}/users`, user).pipe(
      map(response => {
        const newUser = response.data ?? null;
        if (newUser) {
          const currentUsers = [...this.usersSubject.value, newUser];
          this.usersSubject.next(currentUsers);
        }
        return response;
      })
    );
  }

  updateUser(user: User): Observable<ApiResponse<User> | null> {
    return this.http.patch<ApiResponse<User>>(`${this.apiUrl}/users/${user.id}`, user).pipe(
      map(response => {
        const updatedUser = response.data;
        if (updatedUser) {
          const currentUsers = this.usersSubject.value.map(u => u.id === updatedUser.id ? updatedUser : u);
          this.usersSubject.next(currentUsers);
        }
        return response;
      }));
  }

  deleteUser(userId: number): Observable<boolean> {
    return this.http.delete(`${this.apiUrl}/users/${userId}`).pipe(
      map(() => {
        const currentUsers = this.usersSubject.value.filter(user => user.id !== userId);
        this.usersSubject.next(currentUsers);
        if (this.selectedUserSubject.value?.id === userId) {
          this.selectedUserSubject.next(null);
        }
        return true;
      }),
    );
  }
}
