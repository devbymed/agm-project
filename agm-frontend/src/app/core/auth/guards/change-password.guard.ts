import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../auth.service';

export const changePasswordGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  const authResponse = authService.authResponse;

  if (!authResponse) {
    router.navigate(['/auth/connexion']);
    return false;
  }

  if (!authResponse.mustChangePassword) {
    router.navigate(['/accueil/preparation-assemblee/nouvelle-assemblee']);
    return false;
  }

  return true;
};
