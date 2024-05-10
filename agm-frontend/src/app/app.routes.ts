import { Routes } from '@angular/router';
import LoginPage from './pages/login/login.page';
import UserManagementPage from './pages/user-management/user-management.page';
import { LayoutComponent } from './shared/components/layout.component';

export const routes: Routes = [
  {
    path: 'login',
    // loadComponent: () => import('./pages/login/login.page'),
    component: LoginPage,
    // canActivate: [guestGuard],
  },
  // {
  //   path: 'user-management',
  //   loadComponent: () => import('./pages/user-management/user-management.page'),
  //   canActivate: [authGuard],
  // },
  {
    path: 'dashboard',
    component: LayoutComponent,
    children: [
      {
        path: 'user-management',
        component: UserManagementPage,
      },
    ],
  },
  {
    path: '',
    redirectTo: 'dashboard',
    pathMatch: 'full',
  },
];
