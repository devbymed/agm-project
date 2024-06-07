import { ErrorHandler, Injectable, NgZone, inject } from '@angular/core';
import { LoggingService } from "./logging.service";

@Injectable({
  providedIn: 'root'
})
export class ErrorHandlerService implements ErrorHandler {
  private loggingService = inject(LoggingService);
  private zone = inject(NgZone);

  handleError(error: any): void {
    this.loggingService.logError(error);
    this.zone.run(() => {
      console.error(error);
    });
  }
}
