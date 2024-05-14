import { Routes } from '@angular/router';
import { MainLayoutComponent } from './main-layout.component';
import LoginPage from './pages/login/login.page';
import { MembersConvocationComponent } from './pages/members-convocation/members-convocation.component';
import { MembersListComponent } from './pages/members-convocation/pages/members-list/members-list.component';
import { AssemblyDetailsComponent } from './pages/new-assembly/assembly-details/assembly-details.component';
import { FdrFollowUpComponent } from './pages/new-assembly/fdr-follow-up/fdr-follow-up.component';
import { NewAssemblyComponent } from './pages/new-assembly/new-assembly.component';
import { sidebarLinksResolver } from './sidebar-links.resolver';

export const routes: Routes = [
  {
    path: 'connexion',
    component: LoginPage,
  },
  {
    path: '',
    redirectTo: 'nouvelle-assemblee',
    pathMatch: 'full',
  },
  {
    path: '',
    component: MainLayoutComponent,
    children: [
      {
        path: 'nouvelle-assemblee',
        component: NewAssemblyComponent,
        resolve: {
          links: sidebarLinksResolver,
        },
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
        resolve: {
          links: sidebarLinksResolver,
        },
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
];
