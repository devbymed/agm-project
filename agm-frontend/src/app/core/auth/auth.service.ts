import {inject, Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {StorageService} from "@core/services/storage.service";
import {environment} from "@env/environment";
import {BehaviorSubject, catchError, of, tap} from "rxjs";
import {AuthResponse} from "@core/auth/models/auth-response.model";
import {AuthRequest} from "@core/auth/models/auth-request.model";
import {ApiResponse} from "@core/models/api-response.model";
import {User} from "@core/models/user.model";

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private http = inject(HttpClient);
  private storageService = inject(StorageService);

  private apiUrl = environment.apiUrl;
  private accessToken: string | null = null;
  private mustChangePassword = false;
  private loginResponseSubject = new BehaviorSubject<AuthResponse | null>(null);
  loginResponse$ = this.loginResponseSubject.asObservable();
  private errorSubject = new BehaviorSubject<string | null>(null);
  error$ = this.errorSubject.asObservable();

  constructor() {
    this.loadStoredData();
  }

  private loadStoredData(): void {
    this.accessToken = this.storageService.getItem<string>('accessToken');
    const user = this.storageService.getItem<AuthResponse>('user');
    const mustChangePassword = this.storageService.getItem<boolean>('mustChangePassword');

    if (this.accessToken && user) {
      this.loginResponseSubject.next(user);
    }

    if (mustChangePassword !== null) {
      this.mustChangePassword = mustChangePassword;
    }
  }

  login(credentials: AuthRequest): void {
    this.http.post<ApiResponse<AuthResponse>>(`${this.apiUrl}/auth/login`, credentials)
      .pipe(
        tap(response => {
          if(response.status === "OK" && response.data) {
            this.accessToken = response.data.accessToken;
            this.storageService.setItem<string>('accessToken', this.accessToken);
            this.storageService.setItem<User>('user', response.data.user);
            this.storageService.setItem<boolean>('mustChangePassword', response.data.mustChangePassword);
            this.loginResponseSubject.next(response.data);
            this.errorSubject.next(null);
          } else {
            this.loginResponseSubject.next(null);
            this.errorSubject.next(response.message || 'Une erreur est survenue');
          }
        }),
        catchError(error => {
          console.log('Erreur lors de la connexion', error);
          this.loginResponseSubject.next(null);
          this.errorSubject.next('Une erreur est survenue');
          return of(null);
        })
      ).subscribe();
  }

  logout(): void {
    this.accessToken = null;
    this.storageService.removeItem('accessToken');
    this.storageService.removeItem('user');
    this.storageService.removeItem('mustChangePassword');
    this.loginResponseSubject.next(null);
  }

  get token(): string | null {
    return this.accessToken;
  }

  get user(): AuthResponse | null {
    return this.loginResponseSubject.value;
  }

  get mustChangePasswordFlag(): boolean {
    const storedMustChangePassword = this.storageService.getItem<boolean>('mustChangePassword');
    return storedMustChangePassword ? storedMustChangePassword : false;
  }
}
