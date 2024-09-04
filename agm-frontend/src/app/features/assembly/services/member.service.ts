import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ApiResponse } from '@core/models/api-response.model';
import { environment } from '@env/environment';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { MemberEligibility } from '../models/member-eligibility';
import { Member } from '../models/member.model';

@Injectable({
  providedIn: 'root',
})
export class MemberService {
  private apiUrl = environment.apiUrl;

  private memberStatusUpdatedSource = new BehaviorSubject<Member[] | null>(
    null,
  );
  memberStatusUpdated$ = this.memberStatusUpdatedSource.asObservable();

  constructor(private http: HttpClient) {}

  generateDocuments(memberId: number): Observable<ApiResponse<any>> {
    return this.http.post<ApiResponse<any>>(
      `${this.apiUrl}/members/generate-documents`,
      { memberId },
    );
  }

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

  searchMember(
    memberNumber: string,
  ): Observable<ApiResponse<MemberEligibility>> {
    return this.http.get<ApiResponse<MemberEligibility>>(
      `${this.apiUrl}/members/search/${memberNumber}`,
    );
  }

  assignMembersToAgent(
    memberNumbers: string[],
    agentId: number,
  ): Observable<ApiResponse<void>> {
    return this.http.post<ApiResponse<void>>(`${this.apiUrl}/members/assign`, {
      memberNumbers,
      agentId,
    });
  }

  autoAssignMembers(): Observable<ApiResponse<void>> {
    return this.http.post<ApiResponse<void>>(
      `${this.apiUrl}/members/auto-assign`,
      {},
    );
  }

  reassignAgent(
    memberNumber: string,
    newAgentId: number,
  ): Observable<ApiResponse<void>> {
    return this.http.patch<ApiResponse<void>>(
      `${this.apiUrl}/members/reassign-agent`,
      null,
      {
        params: {
          memberNumber: memberNumber,
          newAgentId: newAgentId.toString(),
        },
      },
    );
  }

  notifyMemberStatusUpdate(members: Member[]) {
    this.memberStatusUpdatedSource.next(members);
  }

  downloadFile(filepath: string) {
    const fileUrl = `${this.apiUrl}/members/download?filepath=${encodeURIComponent(filepath)}`;
    this.http
      .get(fileUrl, { responseType: 'blob' })
      .pipe(
        tap({
          next: (blob) => {
            const blobUrl = window.URL.createObjectURL(blob);
            const a = document.createElement('a');
            a.href = blobUrl;
            a.download = filepath.split('/').pop()!;
            document.body.appendChild(a);
            a.click();
            document.body.removeChild(a);
            window.URL.revokeObjectURL(blobUrl);
          },
          error: (error) => {
            console.error('Error downloading the file:', error);
          },
        }),
      )
      .subscribe();
  }
}
