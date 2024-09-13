import { CommonModule, DatePipe } from '@angular/common';
import { Component } from '@angular/core';
import { Member } from '@features/assembly/models/member.model';
import { MemberService } from '@features/assembly/services/member.service';
import jsPDF from 'jspdf';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-invitation-reports',
  standalone: true,
  imports: [CommonModule, DatePipe],
  templateUrl: './invitation-reports.component.html',
  styles: ``,
})
export class InvitationReportsComponent {
  members: Member[] = [];

  constructor(
    private memberService: MemberService,
    private toastr: ToastrService,
  ) {}

  ngOnInit(): void {
    this.loadMembers();

    this.memberService.memberStatusUpdated$.subscribe((updatedMembers) => {
      if (updatedMembers) {
        this.members = updatedMembers;
      }
    });
  }

  loadMembers(): void {
    this.memberService.getEligibleMembers().subscribe({
      next: (response) => {
        if (response.status === 'OK') {
          this.members = response.data || [];
        } else {
          this.toastr.error('Erreur lors de la récupération des membres.');
        }
      },
      error: (error) => {
        console.error('Erreur lors de la récupération des membres', error);
        this.toastr.error('Erreur lors de la récupération des membres.');
      },
    });
  }

  getEditRate(): number {
    const totalMembers = this.members.length;
    const editedMembers = this.members.filter(
      (member) => member.status === 'Editée',
    ).length;
    return totalMembers > 0 ? (editedMembers / totalMembers) * 100 : 0;
  }

  exportPDF(): void {
    const doc = new jsPDF();
    // Définir les colonnes que tu veux inclure dans le PDF
    const columns = [
      { header: 'N°Adhérent', dataKey: 'memberNumber' },
      { header: 'Type', dataKey: 'type' },
      { header: 'Raison sociale', dataKey: 'companyName' },
      { header: 'Adresse1', dataKey: 'address1' },
      { header: 'Adresse2', dataKey: 'address2' },
      { header: 'Ville', dataKey: 'city' },
      { header: 'Agent', dataKey: 'agentFullName' },
      { header: 'Statut', dataKey: 'status' },
      { header: 'Date affectation', dataKey: 'assignmentDate' },
      { header: 'Date édition', dataKey: 'editionDate' },
    ];

    // Définir les données à inclure explicitement
    const rows = this.members.map((member) => [
      member.memberNumber,
      member.type,
      member.companyName,
      member.address1,
      member.address2,
      member.city,
      member.agentFullName,
      member.status,
      member.assignmentDate,
      member.editionDate,
    ]);

    // Utiliser jsPDF AutoTable pour générer le PDF
    (doc as any).autoTable({
      head: [columns.map((col) => col.header)], // En-têtes de colonnes
      body: rows, // Données des membres sans utiliser les index dynamiques
      theme: 'grid',
      font: 'Inter',
      headStyles: {
        fillColor: [119, 173, 26],
        textColor: [255, 255, 255],
      },
      styles: {
        fontSize: 5,
      },
    });

    doc.save('états_convocations.pdf');
  }

  exportCSV(): void {
    const headers = [
      'N° Adhérent',
      'Type',
      'Raison sociale',
      'Adresse 1',
      'Adresse 2',
      'Ville',
      'Agent',
      'Statut',
      'Date affectation',
      'Date édition',
    ];

    // Créer les lignes du CSV
    const rows = this.members.map((member) => [
      member.memberNumber,
      member.type,
      member.companyName,
      member.address1,
      member.address2,
      member.city,
      member.agentFullName,
      member.status,
      member.assignmentDate,
      member.editionDate,
    ]);

    // Générer le contenu du CSV avec un BOM pour UTF-8
    const csvContent = [
      '\ufeff' + headers.join(','), // Ajouter le BOM UTF-8 et les en-têtes
      ...rows.map((e) => e.join(',')), // Ajouter les lignes
    ].join('\n');

    // Créer un objet Blob avec le contenu CSV
    const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' });
    const url = window.URL.createObjectURL(blob);

    // Créer un lien pour télécharger le fichier CSV
    const link = document.createElement('a');
    link.setAttribute('href', url);
    link.setAttribute('download', 'états_convocations.csv');
    link.style.visibility = 'hidden';

    // Ajouter le lien à la page, le cliquer, puis le supprimer
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  }
}
