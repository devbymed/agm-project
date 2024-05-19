import {CanActivateFn, Router} from '@angular/router';
import {inject} from "@angular/core";
import {AuthService} from "@core/auth/auth.service";
import {catchError, map, of} from "rxjs";

export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  return authService.loginResponse$.pipe(
    map(user => {
      if (!user) {
        router.navigateByUrl('/auth/connexion').then(r => console.log('Redirection vers la page de connexion'), e => console.error(e));
        return false;
      }

      if (user.mustChangePassword) {
        router.navigateByUrl('/auth/changer-mot-de-passe').then(r => console.log('Redirection vers la page de changement de mot de passe'), e => console.error(e));
        return false;
      }

      return true;
    }),
    catchError(() => {
      router.navigateByUrl('/auth/connexion').then(r => console.log('Redirection vers la page de connexion'), e => console.error(e));
      return of(false);
    })
  );
};
