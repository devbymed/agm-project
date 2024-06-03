import { ErrorHandler, Injectable, NgZone, inject } from '@angular/core';
import { LoggingService } from "./logging.service";
import { ToastService } from "./toast.service";

@Injectable({
  providedIn: 'root'
})
export class ErrorHandlerService implements ErrorHandler {
  private loggingService = inject(LoggingService);
  private toastService = inject(ToastService);
  private zone = inject(NgZone);

  handleError(error: any): void {
    this.loggingService.logError(error);
    this.zone.run(() => {
      this.toastService.showToast({
        type: 'error',
        message: 'Une erreur est survenue'
      });
    });
  }
}
