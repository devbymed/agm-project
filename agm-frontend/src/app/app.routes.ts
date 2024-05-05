import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';
import { guestGuard } from './core/guards/guest.guard';
import { LoginPage } from './pages/login/login.page';
import { UserManagementPage } from './pages/user-management/user-management.page';

export const routes: Routes = [
  {
    path: 'login',
    component: LoginPage,
    canActivate: [guestGuard],
  },
  {
    path: 'user-management',
    component: UserManagementPage,
    canActivate: [authGuard],
  },
  {
    path: '',
    redirectTo: 'login',
    pathMatch: 'full',
  },
  // {
  //   path: '',
  //   component: LayoutComponent,
  //   children: [
  //     {
  //       path: 'user-management',
  //       component: UserManagementPage,
  //       canActivate: [authGuard],
  //     },
  //   ],
  // },
];
