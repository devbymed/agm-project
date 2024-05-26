import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from "@angular/core";
import { StorageService } from "@core/services/storage.service";

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const storageService = inject(StorageService);
  const accessToken = storageService.get('accessToken');

  if (accessToken) {
    const clonedReq = req.clone({
      setHeaders: {
        Authorization: `Bearer ${accessToken}`
      }
    });
    return next(clonedReq);
  }

  return next(req);
};
