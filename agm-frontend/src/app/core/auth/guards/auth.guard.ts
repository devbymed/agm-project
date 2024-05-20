import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '@core/auth/auth.service';

export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  const authResponse = authService.authResponse;

  if (!authResponse) {
    router.navigate(['/auth/connexion']);
    return false;
  }

  if (authResponse.mustChangePassword) {
    router.navigate(['/auth/changer-mot-de-passe']);
    return false;
  }

  return true;
};
