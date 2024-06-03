import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class LoggingService {
  logError(error: any): void {
    console.error(error);
  }
}
