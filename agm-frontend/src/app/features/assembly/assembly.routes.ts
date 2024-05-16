import { Routes } from '@angular/router';
import AssemblyDetailsPage from './pages/assembly-details/assembly-details.page';
import FdrFollowUpPage from './pages/fdr-follow-up/fdr-follow-up.page';
import MembersConvocationPage from './pages/members-convocation/members-convocation.page';
import NewAssemblyPage from './pages/new-assembly/new-assembly.page';

export default [
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
    component: MembersConvocationPage,
  },
] as Routes;
