import { Routes } from '@angular/router';
import AuthorizationsPage from './pages/authorizations/authorizations.page';
import SettingsPage from './pages/settings/settings.page';
import UsersPage from './pages/users/users.page';

export default [
  {
    path: 'utilisateurs',
    component: UsersPage,
  },
  {
    path: 'habilitations',
    component: AuthorizationsPage,
  },
  {
    path: 'parametrage',
    component: SettingsPage,
  },
  {
    path: '',
    redirectTo: 'utilisateurs',
    pathMatch: 'full',
  },
] as Routes;
