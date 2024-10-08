import { Routes } from "@angular/router";
import { authGuard } from "@core/auth/guards/auth.guard";
import { changePwdGuard } from "@core/auth/guards/change-pwd.guard";
import { firstLoginGuard } from "@core/auth/guards/first-login.guard";
import { guestGuard } from "@core/auth/guards/guest.guard";
import ChangePasswordComponent from "@core/auth/pages/change-password/change-password.component";
import LoginComponent from "@core/auth/pages/login/login.component";
import { AuthorizationsComponent } from "@features/admin/pages/authorizations/authorizations.component";
import { SettingsComponent } from "@features/admin/pages/settings/settings.component";
import { UserManagementComponent } from "@features/admin/pages/user-management/user-management.component";
import { currentAssemblyGuard } from "@features/assembly/guards/current-assembly.guard";
import { AssemblyDetailsComponent } from "@features/assembly/pages/assembly-details/assembly-details.component";
import { FdrFollowUpComponent } from "@features/assembly/pages/fdr-follow-up/fdr-follow-up.component";
import { InvitationsComponent } from "@features/assembly/pages/invitations/invitations.component";
import { MailManagementComponent } from "@features/assembly/pages/mail-management/mail-management.component";
import {
  MembersConvocationComponent
} from "@features/assembly/pages/members-convocation/members-convocation.component";
import { MembersListComponent } from "@features/assembly/pages/members-list/members-list.component";
import { NewAssemblyComponent } from "@features/assembly/pages/new-assembly/new-assembly.component";
import { MainLayoutComponent } from "./core/layout/main-layout/main-layout.component";

export const routes: Routes = [

  {
    path: "connexion",
    component: LoginComponent,
    canActivate: [guestGuard]
  },
  {
    path: "changer-mot-de-passe",
    component: ChangePasswordComponent,
    canActivate: [authGuard, changePwdGuard]
  },
  {
    path: "",
    component: MainLayoutComponent,
    data: { breadcrumb: "Accueil" },
    canActivate: [authGuard, firstLoginGuard],
    children: [
      {
        path: "preparation-assemblee",
        data: { breadcrumb: "Préparation assemblée" },
        children: [
          {
            path: "nouvelle-assemblee",
            component: NewAssemblyComponent,
            data: { breadcrumb: "Nouvelle assemblée" },
            children: [
              {
                path: "assemblee-en-cours",
                component: AssemblyDetailsComponent,
                canActivate: [currentAssemblyGuard],
                data: { breadcrumb: "Assemblée en cours" }
              },
              {
                path: "suivi-fdr",
                component: FdrFollowUpComponent,
                canActivate: [currentAssemblyGuard],
                data: { breadcrumb: "Suivi FDR" }
              },
            ]
          },
          {
            path: "convocation-adherents",
            component: MembersConvocationComponent,
            data: { breadcrumb: "Convocation des adhérents" },
            children: [
              {
                path: "liste-adherents",
                component: MembersListComponent,
                data: { breadcrumb: "Liste des adhérents" }
              },
              {
                path: "convocation",
                component: InvitationsComponent,
                data: { breadcrumb: "Convocation" }
              },
              {
                path: "gestion-retours-courriers",
                component: MailManagementComponent,
                data: { breadcrumb: "Gestion des retours courriers" }
              },
              {
                path: "",
                redirectTo: "liste-adherents",
                pathMatch: "full"
              }
            ]
          },
          {
            path: "",
            redirectTo: "nouvelle-assemblee",
            pathMatch: "full"
          }
        ]
      },
      {
        path: "administration",
        data: { breadcrumb: "Administration" },
        children: [
          {
            path: "habilitations",
            component: AuthorizationsComponent,
            data: { breadcrumb: "Habilitations" }
          },
          {
            path: "utilisateurs",
            component: UserManagementComponent,
            data: { breadcrumb: "Gestion utilisateurs" }
          },
          {
            path: "parametrage",
            component: SettingsComponent,
            data: { breadcrumb: "Paramétrage" }
          },
          {
            path: "",
            redirectTo: "habilitations",
            pathMatch: "full"
          }
        ]
      },
      {
        path: "",
        redirectTo: "preparation-assemblee",
        pathMatch: "full"
      }
    ]
  },
  {
    path: "**",
    redirectTo: ""
  }
];
