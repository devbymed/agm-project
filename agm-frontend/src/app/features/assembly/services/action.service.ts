import { HttpClient } from "@angular/common/http";
import { Injectable } from '@angular/core';
import { ApiResponse } from "@core/models/api-response.model";
import { environment } from "@env/environment";
import { Observable } from "rxjs";
import { Action } from "../models/action.model";

@Injectable({
  providedIn: 'root'
})
export class ActionService {
  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) { }

  getActions(): Observable<ApiResponse<Action[]>> {
    return this.http.get<ApiResponse<Action[]>>(`${this.apiUrl}/actions`);
  }

  getActionById(id: string): Observable<ApiResponse<Action>> {
    return this.http.get<ApiResponse<Action>>(`${this.apiUrl}/actions/${id}`);
  }

  updateAction(id: number, data: Action): Observable<ApiResponse<Action>> {
    return this.http.patch<ApiResponse<Action>>(`${this.apiUrl}/actions/${id}`, data);
  }
}
