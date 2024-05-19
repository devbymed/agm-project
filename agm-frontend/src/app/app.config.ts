import {ApplicationConfig} from '@angular/core';
import {provideRouter} from '@angular/router';

import {provideHttpClient, withInterceptors} from '@angular/common/http';
import {routes} from './app.routes';
import {withCredentialsInterceptor} from "@core/interceptors/http.interceptor";
import {authInterceptor} from "@core/auth/auth.interceptor";

export const appConfig: ApplicationConfig = {
  providers: [provideRouter(routes), provideHttpClient(withInterceptors([authInterceptor]))],
};
