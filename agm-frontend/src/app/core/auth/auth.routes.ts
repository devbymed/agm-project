import { Route } from '@angular/router';
import ChangePasswordPage from './pages/change-password/change-password.page';
import LoginPage from './pages/login/login.page';
import {authGuard} from "@core/auth/auth.guard";

export default [
  {
    path: 'connexion',
    component: LoginPage,
  },
  {
    path: 'changer-mot-de-passe',
    component: ChangePasswordPage,
    canActivate: [authGuard]
  },
  {
    path: '',
    redirectTo: 'connexion',
    pathMatch: 'full',
  },
] as Route[];
