import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ApiResponse } from '@core/models/api-response.model';
import { environment } from '@env/environment';
import { Observable } from 'rxjs';
import { Member } from '../models/member.model';

@Injectable({
  providedIn: 'root',
})
export class MemberService {
  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  getEligibleMembers(): Observable<ApiResponse<Member[]>> {
    return this.http.get<ApiResponse<Member[]>>(`${this.apiUrl}/members`);
  }

  updateMember(
    memberNumber: string,
    updateData: Partial<Member>,
  ): Observable<ApiResponse<Member>> {
    return this.http.patch<ApiResponse<Member>>(
      `${this.apiUrl}/members/${memberNumber}`,
      updateData,
    );
  }
}
