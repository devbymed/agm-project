import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { AuthRequest } from '@core/auth/models/auth-request.model';
import { AuthResponse } from '@core/auth/models/auth-response.model';
import { ApiResponse } from '@core/models/api-response.model';
import { User } from '@core/models/user.model';
import { StorageService } from '@core/services/storage.service';
import { environment } from '@env/environment';
import { BehaviorSubject, catchError, tap, throwError } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private http = inject(HttpClient);
  private storageService = inject(StorageService);
  private apiUrl = environment.apiUrl;
  private loginResponseSubject = new BehaviorSubject<AuthResponse | null>(null);
  loginResponse$ = this.loginResponseSubject.asObservable();
  private errorSubject = new BehaviorSubject<string | null>(null);
  error$ = this.errorSubject.asObservable();

  constructor() {
    this.loadStoredData();
  }

  private loadStoredData(): void {
    const accessToken = this.storageService.getItem<string>('accessToken');
    const user = this.storageService.getItem<User>('user');
    const mustChangePassword =
      this.storageService.getItem<boolean>('mustChangePassword');

    if (accessToken && user) {
      this.loginResponseSubject.next({
        accessToken,
        user,
        mustChangePassword: mustChangePassword ?? false,
      });
    }
  }

  login(credentials: AuthRequest): void {
    this.http
      .post<ApiResponse<AuthResponse>>(`${this.apiUrl}/auth/login`, credentials)
      .pipe(
        tap((response) => {
          if (response.status === 'OK' && response.data) {
            this.storageService.setItem<string>(
              'accessToken',
              response.data.accessToken,
            );
            this.storageService.setItem<User>('user', response.data.user);
            this.storageService.setItem<boolean>(
              'mustChangePassword',
              response.data.mustChangePassword,
            );
            this.loginResponseSubject.next(response.data);
            this.errorSubject.next(null);
          } else {
            this.loginResponseSubject.next(null);
            this.errorSubject.next(
              response.message || 'Une erreur est survenue',
            );
          }
        }),
        catchError((error) => {
          console.log('Erreur lors de la connexion', error);
          const errorMessage =
            error.error?.message || 'Une erreur est survenue';
          this.loginResponseSubject.next(null);
          this.errorSubject.next(errorMessage);
          return throwError(() => error);
        }),
      )
      .subscribe();
  }

  logout(): void {
    this.storageService.removeItem('accessToken');
    this.storageService.removeItem('user');
    this.storageService.removeItem('mustChangePassword');
    this.loginResponseSubject.next(null);
  }

  get token(): string | null {
    return this.storageService.getItem<string>('accessToken');
  }

  get authResponse(): AuthResponse | null {
    return this.loginResponseSubject.value;
  }

  get mustChangePasswordFlag(): boolean {
    return this.authResponse?.mustChangePassword ?? false;
  }
}
