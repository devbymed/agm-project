import { Routes } from '@angular/router';
import { ChangePasswordPage } from './pages/change-password/change-password.page';
import { LoginPage } from './pages/login/login.page';

export const routes: Routes = [
  {
    path: 'login',
    component: LoginPage,
  },
  {
    path: 'change-password',
    component: ChangePasswordPage,
  },
];
