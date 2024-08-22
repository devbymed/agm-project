import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ApiResponse } from '@core/models/api-response.model';
import { environment } from '@env/environment';
import { Observable } from 'rxjs';
import { Action } from '../models/action.model';

@Injectable({
  providedIn: 'root',
})
export class ActionService {
  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  getActions(): Observable<ApiResponse<Action[]>> {
    return this.http.get<ApiResponse<Action[]>>(`${this.apiUrl}/actions`);
  }

  getActionById(id: string): Observable<ApiResponse<Action>> {
    return this.http.get<ApiResponse<Action>>(`${this.apiUrl}/actions/${id}`);
  }

  updateAction(id: number, data: FormData): Observable<ApiResponse<Action>> {
    return this.http.patch<ApiResponse<Action>>(
      `${this.apiUrl}/actions/${id}`,
      data,
      {
        headers: new HttpHeaders({
          Accept: 'application/json',
        }),
      },
    );
  }

  downloadAttachment(actionId: number, fileName: string): void {
    const url = `${this.apiUrl}/actions/${actionId}/attachments/${fileName}`;

    this.http.get(url, { responseType: 'blob' }).subscribe((blob: Blob) => {
      const originalFileName = fileName.substring(fileName.indexOf('_') + 1); // Extraire le nom original sans UUID

      const downloadLink = document.createElement('a');
      const objectUrl = URL.createObjectURL(blob);
      downloadLink.href = objectUrl;
      downloadLink.download = originalFileName; // Utiliser le nom de fichier original
      downloadLink.click();
      URL.revokeObjectURL(objectUrl); // Libérer l'URL après utilisation
    });
  }

  getReadableFileType(fileType: string): string {
    if (!fileType) return 'Unknown';

    const typeParts = fileType.split('/');
    switch (typeParts[1]) {
      case 'pdf':
        return 'PDF';
      case 'msword':
      case 'vnd.openxmlformats-officedocument.wordprocessingml.document':
        return 'Word';
      case 'vnd.ms-excel':
      case 'vnd.openxmlformats-officedocument.spreadsheetml.sheet':
        return 'Excel';
      case 'jpeg':
      case 'jpg':
        return 'JPEG';
      case 'png':
        return 'PNG';
      // Ajoutez d'autres types de fichiers ici si nécessaire
      default:
        return typeParts[0].charAt(0).toUpperCase() + typeParts[0].slice(1);
    }
  }
}
