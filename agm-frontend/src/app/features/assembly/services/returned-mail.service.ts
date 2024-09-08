import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ApiResponse } from '@core/models/api-response.model';
import { environment } from '@env/environment';
import { Observable } from 'rxjs';
import { ReturnedMail } from '../models/returned-mail';

@Injectable({
  providedIn: 'root',
})
export class ReturnedMailService {
  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  createReturnedMail(newReturnedMail: {
    memberNumber: string;
    returnDate: string;
    reasonId: number;
  }): Observable<ApiResponse<void>> {
    return this.http.post<ApiResponse<void>>(
      `${this.apiUrl}/returned-mails`,
      newReturnedMail,
    );
  }

  // Récupérer la liste des courriers retournés
  getReturnedMails(): Observable<ApiResponse<ReturnedMail[]>> {
    return this.http.get<ApiResponse<ReturnedMail[]>>(
      `${this.apiUrl}/returned-mails`,
    );
  }
}
