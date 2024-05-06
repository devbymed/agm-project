import { Routes } from '@angular/router';
import LoginPage from './pages/login/login.page';

export const routes: Routes = [
  {
    path: 'login',
    // loadComponent: () => import('./pages/login/login.page'),
    component: LoginPage,
    // canActivate: [guestGuard],
  },
  {
    path: 'user-management',
    loadComponent: () => import('./pages/user-management/user-management.page'),
    // canActivate: [authGuard],
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
