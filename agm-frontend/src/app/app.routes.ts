import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: 'accueil',
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
    path: 'auth',
    loadChildren: () => import('@core/auth/auth.routes'),
  },
  {
    path: '',
    redirectTo: 'accueil/preparation-assemblee/nouvelle-assemblee',
    pathMatch: 'full',
  },
];
