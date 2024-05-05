import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { map } from 'rxjs';
import { ApiResponse } from '../models/api-response.model';
import { AuthRequest, AuthResponse } from '../models/auth.model';
import { TokenService } from './token.service';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private http = inject(HttpClient);
  private tokenService = inject(TokenService);

  login(data: AuthRequest) {
    return this.http
      .post<
        ApiResponse<AuthResponse>
      >('http://localhost:8080/api/v1/auth/login', data)
      .pipe(
        map((response) => {
          if (response.data?.accessToken) {
            this.tokenService.setToken(response.data.accessToken);
          }
          return response;
        }),
      );
  }
}
