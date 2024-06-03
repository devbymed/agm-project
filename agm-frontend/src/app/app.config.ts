import { ApplicationConfig, ErrorHandler } from '@angular/core';
import { provideRouter } from '@angular/router';

import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { authInterceptor } from "@core/auth/auth.interceptor";
import { ErrorHandlerService } from "@core/services/error-handler.service";
import { routes } from './app.routes';

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    provideHttpClient(withInterceptors([
      authInterceptor
    ])),
    {
      provide: ErrorHandler,
      useClass: ErrorHandlerService,
    }
  ],
};
