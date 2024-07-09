import { HttpClient } from "@angular/common/http";
import { Injectable } from '@angular/core';
import { ApiResponse } from "@core/models/api-response.model";
import { environment } from "@env/environment";
import { Observable } from "rxjs";
import { Assembly } from "../models/assembly.model";

@Injectable({
  providedIn: 'root'
})
export class AssemblyService {
  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) { }

  createAssembly(data: FormData): Observable<ApiResponse<Assembly>> {
    return this.http.post<ApiResponse<Assembly>>(`${this.apiUrl}/assemblies`, data);
  }

  hasCurrentAssembly(): Observable<boolean> {
    return this.http.get<boolean>(`${this.apiUrl}/assemblies/current-exists`);
  }

  deleteCurrentAssembly(): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.apiUrl}/assemblies/current`);
  }
}
