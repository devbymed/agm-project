import { HttpClient } from "@angular/common/http";
import { Injectable, inject } from '@angular/core';
import { ApiResponse } from "@core/models/api-response.model";
import { Profile } from "@core/models/profile.model";
import { environment } from "@env/environment";
import { Observable, catchError, map, of } from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class ProfileService {
  private http = inject(HttpClient);
  private apiUrl = environment.apiUrl;

  getProfiles(): Observable<Profile[]> {
    return this.http.get<ApiResponse<Profile[]>>(`${this.apiUrl}/profiles`).pipe(
      map(response => response.data ?? []),
      catchError(error => {
        console.error('Failed to fetch profiles', error);
        return of([]);
      }));
  }
}
