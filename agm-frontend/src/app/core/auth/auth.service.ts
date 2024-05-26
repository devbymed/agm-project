import { HttpClient } from "@angular/common/http";
import { Injectable, inject } from '@angular/core';
import { AuthResp } from '@core/auth/models/auth-resp.model';
import { ApiResp } from '@core/models/api-resp.model';
import { User } from "@core/models/user.model";
import { StorageService } from "@core/services/storage.service";
import { environment } from '@env/environment';
import {
  Observable,
  tap
} from 'rxjs';
import { AuthReq } from "./models/auth-req.model";

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private http = inject(HttpClient);
  private storageService = inject(StorageService);
  private apiUrl = environment.apiUrl;
  private firstLogin: boolean;

  login(authReq: AuthReq): Observable<ApiResp<AuthResp>> {
    return this.http.post<ApiResp<AuthResp>>(`${this.apiUrl}/auth/login`, authReq)
      .pipe(
        tap((response: ApiResp<AuthResp>) => {
          if (response.data) {
            this.storageService.set('accessToken', response.data.accessToken);
            this.storageService.set('user', response.data.user);
            this.firstLogin = response.data.firstLogin;
          }
        })
      );
  }

  isLoggedIn(): boolean {
    const accessToken = this.storageService.get<string>('accessToken');
    return !!accessToken;
  }

  logout(): void {
    this.storageService.remove('accessToken');
    this.storageService.remove('user');
  }

  getUser(): User | null {
    return this.storageService.get<User>('user');
  }

  isFirstLogin(): boolean {
    return this.firstLogin;
  }
}
