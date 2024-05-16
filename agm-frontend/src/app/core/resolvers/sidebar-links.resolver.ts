import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, ResolveFn } from '@angular/router';
import { SidebarService } from '../services/sidebar.service';

export const sidebarLinksResolver: ResolveFn<void> = (
  route: ActivatedRouteSnapshot,
) => {
  const sidebarService = inject(SidebarService);
  switch (route.url[0].path) {
    case 'nouvelle-assemblee':
      sidebarService.setLinks([
        {
          label: 'Assemblée en cours',
          path: '/accueil/preparation-assemblee/nouvelle-assemblee/assemblee-en-cours',
        },
        {
          label: 'Suivi FDR',
          path: '/accueil/preparation-assemblee/nouvelle-assemblee/suivi-fdr',
        },
      ]);
      break;
    case 'convocation-adherents':
      sidebarService.setLinks([
        { label: 'Liste des adhérents', path: '/convocation-adherents/liste' },
        { label: 'Convocation', path: '/convocation-adherents/convocation' },
        {
          label: 'Regroupements',
          path: '/convocation-adherents/regroupements',
        },
        {
          label: 'Gestion des courriers',
          path: '/convocation-adherents/gestion-courriers',
        },
      ]);
      break;
  }
};
