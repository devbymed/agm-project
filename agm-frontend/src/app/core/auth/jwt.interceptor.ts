import { HttpInterceptorFn } from '@angular/common/http';

export const jwtInterceptor: HttpInterceptorFn = (req, next) => {
  if (req.url.includes('/connexion')) {
    return next(req);
  }
  const token = localStorage.getItem('token');
  if (token) {
    const authReq = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`,
      },
    });
    return next(authReq);
  } else {
    return next(req);
  }
};
