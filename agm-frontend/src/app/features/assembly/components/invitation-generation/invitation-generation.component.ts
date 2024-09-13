import { DatePipe, NgFor, NgIf } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { Member } from '@features/assembly/models/member.model';
import { MemberService } from '@features/assembly/services/member.service';
import { ButtonComponent } from '@shared/components/button/button.component';
import { InputComponent } from '@shared/components/form/input/input.component';
import jsPDF from 'jspdf';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-invitation-generation',
  standalone: true,
  imports: [
    NgIf,
    NgFor,
    DatePipe,
    ReactiveFormsModule,
    InputComponent,
    ButtonComponent,
  ],
  templateUrl: './invitation-generation.component.html',
  styles: ``,
})
export class InvitationGenerationComponent implements OnInit {
  form: FormGroup;
  members: Member[] = [];
  selectedMember: Member | null = null;
  downloadLinks: {
    invitationLetter?: string;
    attendanceSheet?: string;
    proxy?: string;
  } | null = null;

  constructor(
    private fb: FormBuilder,
    private http: HttpClient,
    private memberService: MemberService,
    private toastr: ToastrService,
  ) {}

  ngOnInit(): void {
    this.form = this.fb.group({
      memberNumber: [''],
      companyName: [''],
      address1: [''],
      address2: [''],
      city: [''],
    });

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
          console.log(response.data);
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

    doc.save('convocations.pdf');
  }

  exportCSV(): void {
    const headers = [
      'N° Adhérent',
      'Type',
      'Raison sociale',
      'Adresse 1',
      'Adresse 2',
      'Ville',
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
    link.setAttribute('download', 'convocations.csv');
    link.style.visibility = 'hidden';

    // Ajouter le lien à la page, le cliquer, puis le supprimer
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  }

  onSelectMember(member: Member): void {
    if (
      this.selectedMember &&
      this.selectedMember.memberNumber === member.memberNumber
    ) {
      // Si le même membre est cliqué à nouveau, le désélectionner
      this.selectedMember = null;
    } else {
      // Sinon, mettre à jour le membre sélectionné
      this.selectedMember = member;
    }
    this.updateCheckboxState();
  }

  updateCheckboxState(): void {
    this.members.forEach((m) => {
      if (
        this.selectedMember &&
        m.memberNumber === this.selectedMember.memberNumber
      ) {
        m['isSelected'] = true;
      } else {
        m['isSelected'] = false;
      }
    });
  }

  onEditMember(): void {
    if (this.selectedMember) {
      this.form.patchValue(this.selectedMember);
      console.log('Édition du membre', this.selectedMember.id);
      this.generateDocuments(this.selectedMember.id);
    } else {
      console.error('Aucun membre sélectionné');
    }
  }

  generateDocuments(memberId: number): void {
    console.log('Génération des documents pour le membre', memberId);
    this.memberService.generateDocuments(memberId).subscribe({
      next: (response) => {
        if (response.status === 'OK') {
          this.downloadLinks = response.data;
          this.memberService.getEligibleMembers().subscribe((res) => {
            this.memberService.notifyMemberStatusUpdate(res.data || []);
          });
          this.toastr.success('Documents générés avec succès.');
        } else {
          this.toastr.error('Erreur lors de la génération des documents.');
        }
      },
      error: (error) => {
        console.error('Erreur lors de la génération des documents', error);
        this.toastr.error('Erreur lors de la génération des documents.');
      },
    });
  }

  onDownloadClick(filename: string | undefined): void {
    if (filename) {
      this.memberService.downloadFile(filename);
    }
  }
}
