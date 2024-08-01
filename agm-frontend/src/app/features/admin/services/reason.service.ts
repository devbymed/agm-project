import { HttpClient } from "@angular/common/http";
import { Injectable } from '@angular/core';
import { ApiResponse } from "@core/models/api-response.model";
import { environment } from "@env/environment";
import { Observable } from "rxjs";
import { Reason } from "../models/reason";

@Injectable({
  providedIn: 'root'
})
export class ReasonService {
  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) {
  }

  getReasons(): Observable<ApiResponse<Reason[]>> {
    return this.http.get<ApiResponse<Reason[]>>(`${this.apiUrl}/reasons`);
  }

  // getReason(id: number): Observable<Reason> {
  //   return this.http.get<Reason>(`${this.apiUrl}/reasons/${id}`);
  // }

  createReason(reason: Reason): Observable<ApiResponse<Reason[]>> {
    return this.http.post<ApiResponse<Reason[]>>(`${this.apiUrl}/reasons`, reason);
  }

  updateReason(id: number, reason: Reason): Observable<ApiResponse<Reason[]>> {
    return this.http.patch<ApiResponse<Reason[]>>(`${this.apiUrl}/reasons/${id}`, reason);
  }

  deleteReason(id: number): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.apiUrl}/reasons/${id}`);
  }
}
