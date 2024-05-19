import { Routes } from '@angular/router';
import { authGuard } from '@core/auth/auth.guard';
import { AppShellComponent } from './app-shell.component';

export const routes: Routes = [
  {
    path: 'auth',
    loadChildren: () => import('@core/auth/auth.routes'),
  },
  {
    path: 'accueil',
    component: AppShellComponent,
    canActivate: [authGuard],
    children: [
      {
        path: 'preparation-assemblee',
        loadChildren: () => import('@features/assembly/assembly.routes'),
      },
    ],
    // {
    //   path: 'preparation-assemblee',
    //   children: [
    //     {
    //       path: 'nouvelle-assemblee',
    //       component: NewAssemblyPage,
    //       children: [
    //         {
    //           path: 'assemblee-en-cours',
    //           component: AssemblyDetailsPage,
    //         },
    //         {
    //           path: 'suivi-fdr',
    //           component: FdrFollowUpPage,
    //         },
    //         {
    //           path: '',
    //           redirectTo: 'assemblee-en-cours',
    //           pathMatch: 'full',
    //         },
    //       ],
    //     },
    //     {
    //       path: 'convocation-adherents',
    //       component: MembersConvocationComponent,
    //       children: [
    //         {
    //           path: 'liste',
    //           component: MembersListComponent,
    //         },
    //         {
    //           path: '',
    //           redirectTo: 'liste',
    //           pathMatch: 'full',
    //         },
    //       ],
    //     },
    //   ],
    // },
  },
  {
    path: '',
    redirectTo: 'auth',
    pathMatch: 'full',
  },
];
