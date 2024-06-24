import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { ApiResponse } from "@core/models/api-response.model";
import { environment } from "@env/environment";
import { Observable } from "rxjs";
import { Permission } from "../models/permission.model";

@Injectable({
  providedIn: "root"
})
export class PermissionService {
  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) {
  }

  getPermissionTree(): Observable<ApiResponse<Permission[]>> {
    return this.http.get<ApiResponse<Permission[]>>(`${this.apiUrl}/permissions/hierarchy`);
  }
}
