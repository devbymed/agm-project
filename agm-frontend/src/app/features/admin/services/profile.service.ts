import { HttpClient } from "@angular/common/http";
import { Injectable, inject } from '@angular/core';
import { ApiResponse } from "@core/models/api-response.model";
import { Profile } from "@core/models/profile.model";
import { environment } from "@env/environment";
import { Observable } from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class ProfileService {
  private http = inject(HttpClient);
  private apiUrl = environment.apiUrl;

  getProfiles(): Observable<ApiResponse<Profile[]>> {
    return this.http.get<ApiResponse<Profile[]>>(`${this.apiUrl}/profiles`);
  }
}
