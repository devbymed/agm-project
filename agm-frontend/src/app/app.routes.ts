import { Routes } from '@angular/router';
import { authGuard } from "@core/auth/guards/auth.guard";
import { changePwdGuard } from "@core/auth/guards/change-pwd.guard";
import { firstLoginGuard } from "@core/auth/guards/first-login.guard";
import { guestGuard } from "@core/auth/guards/guest.guard";
import ChangePasswordComponent from "@core/auth/pages/change-password/change-password.component";
import LoginComponent from "@core/auth/pages/login/login.component";
import { AuthorizationsComponent } from '@features/admin/pages/authorizations/authorizations.component';
import { SettingsComponent } from '@features/admin/pages/settings/settings.component';
import { UsersComponent } from '@features/admin/pages/users/users.component';
import { AssemblyDetailsComponent } from '@features/assembly/pages/assembly-details/assembly-details.component';
import { FdrFollowUpComponent } from '@features/assembly/pages/fdr-follow-up/fdr-follow-up.component';
import { MembersConvocationComponent } from '@features/assembly/pages/members-convocation/members-convocation.component';
import { NewAssemblyComponent } from '@features/assembly/pages/new-assembly/new-assembly.component';
import { MainLayoutComponent } from './core/layout/main-layout/main-layout.component';

export const routes: Routes = [

  {
    path: 'connexion',
    component: LoginComponent,
    canActivate: [guestGuard],
  },
  {
    path: 'changer-mot-de-passe',
    component: ChangePasswordComponent,
    canActivate: [authGuard, changePwdGuard],
  },
  {
    path: '',
    component: MainLayoutComponent,
    data: { breadcrumb: 'Accueil' },
    canActivate: [authGuard, firstLoginGuard],
    children: [
      {
        path: 'preparation-assemblee',
        data: { breadcrumb: 'Préparation assemblée' },
        children: [
          {
            path: 'nouvelle-assemblee',
            component: NewAssemblyComponent,
            data: { breadcrumb: 'Nouvelle assemblée' },
            children: [
              {
                path: 'assemblee-en-cours',
                component: AssemblyDetailsComponent,
                data: { breadcrumb: 'Assemblée en cours' },
              },
              {
                path: 'suivi-fdr',
                component: FdrFollowUpComponent,
                data: { breadcrumb: 'Suivi FDR' },
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
            data: { breadcrumb: 'Convocation des adhérents' },
          },
          {
            path: '',
            redirectTo: 'nouvelle-assemblee',
            pathMatch: 'full',
          },
        ],
      },
      {
        path: 'administration',
        data: { breadcrumb: 'Administration' },
        children: [
          {
            path: 'habilitations',
            component: AuthorizationsComponent,
            data: { breadcrumb: 'Habilitations' },
          },
          {
            path: 'utilisateurs',
            component: UsersComponent,
            data: { breadcrumb: 'Gestion utilisateurs' },
          },
          {
            path: 'parametrage',
            component: SettingsComponent,
            data: { breadcrumb: 'Paramétrage' },
          },
          {
            path: '',
            redirectTo: 'habilitations',
            pathMatch: 'full',
          },
        ],
      },
      {
        path: '',
        redirectTo: 'preparation-assemblee',
        pathMatch: 'full',
      },
    ],
  },
  {
    path: '**',
    redirectTo: '',
  },
];
