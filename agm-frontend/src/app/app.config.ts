import { ApplicationConfig, ErrorHandler } from "@angular/core";
import { provideRouter } from "@angular/router";

import { provideHttpClient, withInterceptors } from "@angular/common/http";
import { provideAnimations } from "@angular/platform-browser/animations";
import { authInterceptor } from "@core/auth/auth.interceptor";
import { ErrorHandlerService } from "@core/services/error-handler.service";
import { provideToastr } from "ngx-toastr";
import { routes } from "./app.routes";

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    provideAnimations(),
    provideToastr({
      timeOut: 3000,
      progressBar: true,
      preventDuplicates: true,
      positionClass: "toast-bottom-right"
    }),
    provideHttpClient(withInterceptors([
      authInterceptor
    ])),
    {
      provide: ErrorHandler,
      useClass: ErrorHandlerService
    }
  ]
};
