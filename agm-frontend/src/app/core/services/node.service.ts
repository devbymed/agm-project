import { Injectable } from '@angular/core';
import { TreeNode } from "primeng/api";

@Injectable({
  providedIn: 'root'
})
export class NodeService {
  constructor() { }

  getFiles(): Promise<TreeNode[]> {
    return Promise.resolve([
      {
        label: 'Préparation assemblée',
        expanded: true,
        children: [
          {
            label: 'Nouvelle assemblée',
            children: [
              { label: 'Assemblée en cours' },
              { label: 'Suivi FDR' }
            ]
          },
          {
            label: 'Convocation adhérents',
            children: [
              { label: 'Liste des adhérents' },
              { label: 'Convocation' },
              { label: 'Affectation des adhérents' },
              { label: 'Génération des convocations' },
              { label: 'Consultation des affectations' },
              { label: 'Consultation état des convocation' },
            ]
          },
          {
            label: 'Suivi Quorum',
            children: [
              { label: 'Gestion des relances téléphoniques' },
              { label: 'Affectation des relances' },
              { label: 'Relance des adhérents' },
              { label: 'Consultation des affectations' },
              { label: 'Consultation des relances' }
            ]
          },
          {
            label: 'Numérisation injection des documents',
            children: [
              { label: 'Numérisation' },
              { label: 'Consultation numérisation' },
              { label: 'Qualification des documents' }
            ]
          },
          {
            label: 'Enregistrement adhérents',
            children: [
              { label: 'Assemblée en cours' },
              { label: 'Adhérents convoqués' },
              { label: 'Etat de présence' },
              { label: 'Quorum' },
              { label: 'Dossier juridique' }
            ]
          },
        ]
      }
    ]);
  }
}
