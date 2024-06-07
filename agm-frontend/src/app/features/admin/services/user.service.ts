import { HttpClient } from "@angular/common/http";
import { Injectable, inject } from '@angular/core';
import { ApiResponse } from "@core/models/api-response.model";
import { User } from "@core/models/user.model";
import { environment } from "@env/environment";
import { BehaviorSubject, Observable } from "rxjs";
import { UserAdd } from "../models/user-add";

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private http = inject(HttpClient);
  private apiUrl = environment.apiUrl;
  private selectedUserSubject = new BehaviorSubject<User | null>(null);
  selectedUser$ = this.selectedUserSubject.asObservable();

  getUsers(): Observable<ApiResponse<User[]>> {
    return this.http.get<ApiResponse<User[]>>(`${this.apiUrl}/users`);
  }

  addUser(user: UserAdd): Observable<ApiResponse<User>> {
    return this.http.post<ApiResponse<User>>(`${this.apiUrl}/users`, user);
  }

  selectUser(user: User | null) {
    this.selectedUserSubject.next(user);
  }
}
