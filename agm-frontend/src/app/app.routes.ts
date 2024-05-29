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
    canActivate: [authGuard, firstLoginGuard],
    children: [
      {
        path: 'preparation-assemblee',
        children: [
          {
            path: 'nouvelle-assemblee',
            component: NewAssemblyComponent,
            children: [
              {
                path: 'assemblee-en-cours',
                component: AssemblyDetailsComponent,
              },
              {
                path: 'suivi-fdr',
                component: FdrFollowUpComponent,
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
        children: [
          {
            path: 'utilisateurs',
            component: UsersComponent,
          },
          {
            path: 'habilitations',
            component: AuthorizationsComponent,
          },
          {
            path: 'parametrage',
            component: SettingsComponent,
          },
          {
            path: '',
            redirectTo: 'utilisateurs',
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
