import { Routes } from '@angular/router';
import { authGuard } from '@core/auth/guards/auth.guard';
import { MainLayoutComponent } from './core/layout/main-layout/main-layout.component';

export const routes: Routes = [
  {
    path: 'auth',
    loadChildren: () => import('@core/auth/auth.routes'),
  },
  {
    path: 'accueil',
    component: MainLayoutComponent,
    canActivate: [authGuard],
    children: [
      {
        path: 'preparation-assemblee',
        loadChildren: () => import('@features/assembly/assembly.routes'),
      },
      {
        path: '',
        redirectTo: 'preparation-assemblee',
        pathMatch: 'full',
      },
    ],
  },
  {
    path: '',
    redirectTo: 'accueil',
    pathMatch: 'full',
  },
  {
    path: '**',
    redirectTo: 'accueil',
    pathMatch: 'full',
  },
];
