import { Route } from '@angular/router';
import { changePasswordGuard } from './guards/change-password.guard';
import ChangePasswordPage from './pages/change-password/change-password.page';
import LoginPage from './pages/login/login.page';

export default [
  {
    path: 'connexion',
    component: LoginPage,
  },
  {
    path: 'changer-mot-de-passe',
    component: ChangePasswordPage,
    canActivate: [changePasswordGuard],
  },
  {
    path: '',
    redirectTo: 'connexion',
    pathMatch: 'full',
  },
] as Route[];
