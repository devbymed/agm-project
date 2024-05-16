import { Route } from '@angular/router';
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
  },
  {
    path: '',
    redirectTo: 'connexion',
    pathMatch: 'full',
  },
] as Route[];
