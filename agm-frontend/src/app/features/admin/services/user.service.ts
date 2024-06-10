import { HttpClient } from "@angular/common/http";
import { Injectable } from '@angular/core';
import { ApiResponse } from "@core/models/api-response.model";
import { User } from "@core/models/user.model";
import { environment } from "@env/environment";
import { BehaviorSubject, Observable, tap } from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = environment.apiUrl;

  private usersSubject = new BehaviorSubject<User[]>([]);
  users$ = this.usersSubject.asObservable();

  constructor(private http: HttpClient) { }

  getUsers(): Observable<ApiResponse<User[]>> {
    return this.http.get<ApiResponse<User[]>>(`${this.apiUrl}/users`).pipe(
      tap(response => this.usersSubject.next(response.data || []))
    );
  }

  refreshUsers() {
    this.getUsers().subscribe();
  }

  addUser(user: User): Observable<ApiResponse<any>> {
    return this.http.post<ApiResponse<any>>(`${this.apiUrl}/users`, user).pipe(
      tap(() => this.refreshUsers())
    );
  }

  updateUser(id: number, user: User): Observable<ApiResponse<any>> {
    return this.http.patch<ApiResponse<any>>(`${this.apiUrl}/users/${id}`, user).pipe(
      tap(() => this.refreshUsers())
    );
  }

  deleteUser(id: number): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.apiUrl}/users/${id}`).pipe(
      tap(() => this.refreshUsers())
    );
  }
}
