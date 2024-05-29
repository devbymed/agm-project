import { HttpClient } from "@angular/common/http";
import { Injectable, inject } from '@angular/core';
import { AuthResponse } from '@core/auth/models/auth-response.model';
import { ApiResponse } from '@core/models/api-response.model';
import { User } from "@core/models/user.model";
import { StorageService } from "@core/services/storage.service";
import { environment } from '@env/environment';
import {
  BehaviorSubject,
  Observable,
  tap
} from 'rxjs';
import { AuthRequest } from "./models/auth-request.model";
import { ChangePwdReq } from "./models/change-pwd-req";

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private http = inject(HttpClient);
  private storageService = inject(StorageService);
  private apiUrl = environment.apiUrl;
  private userSubject = new BehaviorSubject<User | null>(this.getUserFromStorage());

  login(authRequest: AuthRequest): Observable<ApiResponse<AuthResponse>> {
    return this.http.post<ApiResponse<AuthResponse>>(`${this.apiUrl}/auth/login`, authRequest).pipe(
      tap(response => {
        if (response.status === 'OK' && response.data) {
          this.setSession(response.data);
        }
      })
    );
  }

  private setSession(authResponse: AuthResponse): void {
    this.storageService.set('accessToken', authResponse.accessToken);
    this.storageService.set('firstLogin', authResponse.firstLogin);
    this.storageService.set('user', authResponse.user);
    this.userSubject.next(authResponse.user);
  }

  isLoggedIn(): boolean {
    return !!this.storageService.get('accessToken');
  }

  getUser(): Observable<User | null> {
    return this.userSubject.asObservable();
  }

  private getUserFromStorage(): User | null {
    return this.storageService.get<User>('user');
  }

  logout(): void {
    this.storageService.clear();
    this.userSubject.next(null);
  }

  isFirstLogin(): boolean {
    return this.storageService.get<boolean>('firstLogin') ?? false;
  }

  changePassword(changePwdReq: ChangePwdReq): Observable<ApiResponse<any>> {
    return this.http.patch<ApiResponse<any>>(`${this.apiUrl}/auth/change-password`, changePwdReq).pipe(
      tap(() => {
        this.storageService.set('firstLogin', false);
      })
    );
  }
}