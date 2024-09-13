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
  selector: 'app-member-allocation',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    SelectComponent,
    ButtonComponent,
    ReactiveFormsModule,
  ],
  templateUrl: './member-allocation.component.html',
})
export class MemberAllocationComponent implements OnInit {
  form: FormGroup;
  agents: SelectOption[] = [];
  members: Member[] = [];
  selectedMemberNumbers: string[] = [];
  assignmentType: 'manual' | 'automatic' = 'manual';

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

  assignMembers(): void {
    if (this.assignmentType === 'manual') {
      if (this.form.valid && this.selectedMemberNumbers.length > 0) {
        this.memberService
          .assignMembersToAgent(
            this.selectedMemberNumbers,
            this.form.value.agent,
          )
          .subscribe({
            next: (response) => {
              if (response.status === 'OK') {
                this.toastr.success(response.message);
                this.form.reset({
                  agent: '',
                });
                this.selectedMemberNumbers = [];
                this.memberService.getEligibleMembers().subscribe((res) => {
                  this.memberService.notifyMemberStatusUpdate(res.data || []);
                });
              }
            },
            error: (error) => {
              console.error("Erreur lors de l'affectation des membres", error);
              this.toastr.error("Erreur lors de l'affectation des membres.");
            },
          });
      } else {
        this.toastr.warning('Veuillez sélectionner un agent et des membres.');
      }
    } else if (this.assignmentType === 'automatic') {
      this.memberService.autoAssignMembers().subscribe({
        next: (response) => {
          if (response.status === 'OK') {
            this.toastr.success(response.message);
            this.selectedMemberNumbers = []; // Clear selection
            this.memberService.getEligibleMembers().subscribe((res) => {
              this.memberService.notifyMemberStatusUpdate(res.data || []);
            });
          }
        },
        error: (error) => {
          console.error("Erreur lors de l'affectation automatique", error);
          this.toastr.error("Erreur lors de l'affectation automatique.");
        },
      });
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
    ];

    // Définir les données à inclure explicitement
    const rows = this.members.map((member) => [
      member.memberNumber,
      member.type,
      member.companyName,
      member.address1,
      member.address2,
      member.city,
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

    doc.save('affectation_adherents.pdf');
  }

  exportCSV(): void {
    const headers = [
      'N° Adhérent',
      'Type',
      'Raison sociale',
      'Adresse 1',
      'Adresse 2',
      'Ville',
    ];

    // Créer les lignes du CSV
    const rows = this.members.map((member) => [
      member.memberNumber,
      member.type,
      member.companyName,
      member.address1,
      member.address2,
      member.city,
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
    link.setAttribute('download', 'affectation_adherents.csv');
    link.style.visibility = 'hidden';

    // Ajouter le lien à la page, le cliquer, puis le supprimer
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  }
}
