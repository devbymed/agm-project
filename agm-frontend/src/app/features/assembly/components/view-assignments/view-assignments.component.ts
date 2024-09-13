import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
} from '@angular/forms';
import { UserService } from '@features/admin/services/user.service';
import { Member } from '@features/assembly/models/member.model';
import { MemberService } from '@features/assembly/services/member.service';
import { ButtonComponent } from '@shared/components/button/button.component';
import { SelectComponent } from '@shared/components/form/select/select.component';
import jsPDF from 'jspdf';
import { ToastrService } from 'ngx-toastr';

interface SelectOption {
  value: string;
  label: string;
}

@Component({
  selector: 'app-view-assignments',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    SelectComponent,
    ButtonComponent,
    ReactiveFormsModule,
  ],
  templateUrl: './view-assignments.component.html',
})
export class ViewAssignmentsComponent implements OnInit {
  form: FormGroup;
  agents: SelectOption[] = [];
  members: Member[] = [];
  selectedMemberNumbers: string[] = [];
  selectedAgentId: string = ''; // New: Store selected agent for reassignment

  constructor(
    private fb: FormBuilder,
    private memberService: MemberService,
    private userService: UserService,
    private toastr: ToastrService,
  ) {}

  ngOnInit(): void {
    this.form = this.fb.group({
      agent: [''],
    });

    this.loadMembers();
    this.loadAgents();

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

  loadAgents(): void {
    this.userService.getAgents().subscribe({
      next: (response) => {
        if (response.status === 'OK') {
          this.agents =
            response.data?.map((agent) => ({
              value: agent.id.toString(),
              label: `${agent.firstName} ${agent.lastName}`,
            })) ?? [];
        }
      },
      error: (error) => {
        console.error('Erreur lors de la récupération des agents', error);
        this.toastr.error('Erreur lors de la récupération des agents.');
      },
    });
  }

  toggleMemberSelection(memberNumber: string, event: Event): void {
    const checkbox = event.target as HTMLInputElement;
    if (checkbox.checked) {
      this.selectedMemberNumbers.push(memberNumber);
    } else {
      const index = this.selectedMemberNumbers.indexOf(memberNumber);
      if (index > -1) {
        this.selectedMemberNumbers.splice(index, 1);
      }
    }
  }

  isSelected(memberNumber: string): boolean {
    return this.selectedMemberNumbers.includes(memberNumber);
  }

  reassignMember(): void {
    if (this.form.valid && this.selectedMemberNumbers.length > 0) {
      this.selectedMemberNumbers.forEach((memberNumber) => {
        this.memberService
          .reassignAgent(memberNumber, this.form.value.agent)
          .subscribe({
            next: (response) => {
              if (response.status === 'OK') {
                this.toastr.success(`Membre réaffecté avec succès`);
                this.selectedMemberNumbers = [];
                this.form.reset({
                  agent: '',
                });
                this.memberService.getEligibleMembers().subscribe((res) => {
                  this.memberService.notifyMemberStatusUpdate(res.data || []);
                });
              } else {
                this.toastr.error('Erreur lors de la réaffectation.');
              }
            },
            error: (error) => {
              console.error(
                'Erreur lors de la réaffectation des membres',
                error,
              );
              this.toastr.error('Erreur lors de la réaffectation des membres.');
            },
          });
      });
    } else {
      this.toastr.warning('Veuillez sélectionner un agent et des membres.');
    }
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

    doc.save('affectations.pdf');
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
    link.setAttribute('download', 'affectations.csv');
    link.style.visibility = 'hidden';

    // Ajouter le lien à la page, le cliquer, puis le supprimer
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  }

  reset(): void {
    this.form.reset({
      agent: '',
    });
    this.selectedMemberNumbers = [];
  }
}
