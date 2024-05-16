import { Routes } from '@angular/router';
import { ChangePasswordPage } from '@core/auth/pages/change-password/change-password.page';
import LoginPage from '@core/auth/pages/login/login.page';
import { AppShellComponent } from './app-shell.component';
import { AssemblyDetailsPage } from './pages/assembly-details/assembly-details.page';
import { FdrFollowUpPage } from './pages/fdr-follow-up/fdr-follow-up.page';
import { MembersConvocationComponent } from './pages/members-convocation/members-convocation.component';
import { MembersListComponent } from './pages/members-list/members-list.component';
import { NewAssemblyPage } from './pages/new-assembly/new-assembly.page';

export const routes: Routes = [
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
    redirectTo: 'accueil/preparation-assemblee/nouvelle-assemblee',
    pathMatch: 'full',
  },
  {
    path: 'accueil',
    component: AppShellComponent,
    children: [
      {
        path: 'preparation-assemblee',
        children: [
          {
            path: 'nouvelle-assemblee',
            component: NewAssemblyPage,
            children: [
              {
                path: 'assemblee-en-cours',
                component: AssemblyDetailsPage,
              },
              {
                path: 'suivi-fdr',
                component: FdrFollowUpPage,
              },
              {
                path: '',
                redirectTo: 'assemblee-en-cours',
                pathMatch: 'full',
              },
            ],
          },
          {
            path: 'convocation-adherents',
            component: MembersConvocationComponent,
            children: [
              {
                path: 'liste',
                component: MembersListComponent,
              },
              {
                path: '',
                redirectTo: 'liste',
                pathMatch: 'full',
              },
            ],
          },
        ],
      },
    ],
  },
];
