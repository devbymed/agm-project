import { inject } from "@angular/core";
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from "../auth.service";

export const firstLoginGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (authService.isFirstLogin()) {
    return true;
  } else {
    router.navigate(['/auth/changer-mot-de-passe']);
    return false;
  }
};
