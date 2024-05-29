import { inject } from "@angular/core";
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from "../auth.service";

export const changePwdGuard: CanActivateFn = () => {
  const authService = inject(AuthService);
  const router = inject(Router);

  const firstLogin = authService.isFirstLogin();
  if (firstLogin) {
    return true;
  } else {
    router.navigate(['/']);
    return false;
  }
};
