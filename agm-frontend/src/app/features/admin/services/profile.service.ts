import { HttpClient } from "@angular/common/http";
import { Injectable } from '@angular/core';
import { ApiResponse } from "@core/models/api-response.model";
import { Profile } from "@core/models/profile.model";
import { environment } from "@env/environment";
import { ProfileEditRequest } from "@features/assembly/models/profile-edit-request";
import { Observable } from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class ProfileService {
  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) { }

  getProfiles(): Observable<ApiResponse<Profile[]>> {
    return this.http.get<ApiResponse<Profile[]>>(`${this.apiUrl}/profiles`);
  }

  addProfile(profileAddRequest: { name: string; permissionIds: number[] }): Observable<ApiResponse<Profile>> {
    return this.http.post<ApiResponse<Profile>>(`${this.apiUrl}/profiles`, profileAddRequest);
  }

  updateProfile(id: number, profileEditRequest: ProfileEditRequest): Observable<ApiResponse<Profile>> {
    return this.http.patch<ApiResponse<Profile>>(`${this.apiUrl}/profiles/${id}`, profileEditRequest);
  }
}
