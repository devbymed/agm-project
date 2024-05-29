import { inject } from "@angular/core";
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from "../auth.service";

export const firstLoginGuard: CanActivateFn = () => {
  const authService = inject(AuthService);
  const router = inject(Router);

  const firstLogin = authService.isFirstLogin();
  if (firstLogin) {
    router.navigate(['/changer-mot-de-passe']);
    return false;
  } else {
    return true;
  }
};
