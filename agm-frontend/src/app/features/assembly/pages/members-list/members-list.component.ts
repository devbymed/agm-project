import { NgFor, NgIf } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { ApiResponse } from '@core/models/api-response.model';
import { MemberEligibility } from '@features/assembly/models/member-eligibility';
import { Member } from '@features/assembly/models/member.model';
import { MemberService } from '@features/assembly/services/member.service';
import { ButtonComponent } from '@shared/components/button/button.component';
import { InputComponent } from '@shared/components/form/input/input.component';
import { Modal } from 'flowbite';
import jsPDF from 'jspdf';
import 'jspdf-autotable';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-members-list',
  standalone: true,
  imports: [NgFor, NgIf, ReactiveFormsModule, InputComponent, ButtonComponent],
  templateUrl: './members-list.component.html',
})
export class MembersListComponent implements OnInit {
  members: Member[] = [];
  selectedMember: Member | null = null;
  editForm: FormGroup;
  searchForm: FormGroup;
  searchMemberModal: Modal | null = null;
  memberEligibility: MemberEligibility | null = null;
  errorMessage: string | null = null;

  constructor(
    private memberService: MemberService,
    private fb: FormBuilder,
    private toastr: ToastrService,
  ) {
    this.editForm = this.fb.group({
      memberNumber: [''],
      companyName: [''],
      address1: [''],
      address2: [''],
      city: [''],
      phone1: [''],
      phone2: [''],
    });

    this.searchForm = this.fb.group({
      memberNumber: ['', [Validators.required]],
    });
  }

  ngOnInit(): void {
    this.initModals();
    this.loadMembers();
  }

  initModals(): void {
    const searchMemberElement = document.getElementById('searchMemberModal');
    if (searchMemberElement) {
      this.searchMemberModal = new Modal(searchMemberElement);
    }
  }

  openSearchMemberModal(): void {
    this.searchForm.reset();
    if (this.searchMemberModal) {
      this.searchMemberModal.show();
    }
  }

  closeSearchMemberModal(): void {
    this.searchForm.reset();
    if (this.searchMemberModal) {
      this.searchMemberModal.hide();
      // this.updateActionForm.reset();
    }
  }

  loadMembers(): void {
    this.memberService.getEligibleMembers().subscribe({
      next: (response: ApiResponse<Member[]>) => {
        if (response.status === 'OK') {
          this.members = response.data || [];
        } else {
          this.errorMessage = response.message || 'Une erreur est survenue.';
        }
      },
      error: (error) => {
        console.error(
          'Erreur lors de la récupération des membres éligibles',
          error,
        );
        this.errorMessage = 'Erreur lors de la récupération des membres.';
      },
    });
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

  onEdit(): void {
    if (this.selectedMember) {
      this.editForm.patchValue(this.selectedMember);
    } else {
      this.toastr.warning('Veuillez sélectionner un membre à modifier.');
      console.error('Aucun membre sélectionné');
    }
  }

  onSubmit(): void {
    if (this.editForm.valid && this.selectedMember) {
      const updatedMember = { ...this.selectedMember, ...this.editForm.value };

      this.memberService
        .updateMember(updatedMember.memberNumber, updatedMember)
        .subscribe({
          next: (response) => {
            this.loadMembers(); // Recharger les membres après la mise à jour
            this.resetForm();
            this.toastr.success(response.message);
          },
          error: (err) => {
            console.error('Erreur lors de la mise à jour du membre', err);
          },
        });
    }
  }

  searchMember(): void {
    if (this.searchForm.valid) {
      const memberNumber = this.searchForm.get('memberNumber')?.value;
      this.memberService.searchMember(memberNumber).subscribe({
        next: (response: ApiResponse<MemberEligibility>) => {
          if (response.status === 'OK') {
            this.memberEligibility = response.data || null;
            this.openSearchMemberModal(); // Ouvrir le modal avec les résultats
          } else {
            this.errorMessage = response.message || 'Adhérent on éligible.';
            this.memberEligibility = null;
            this.openSearchMemberModal(); // Ouvrir le modal pour afficher le message d'erreur
          }
        },
        error: (error) => {
          console.error('Erreur lors de la recherche du membre', error);
          this.errorMessage = 'Erreur lors de la recherche du membre.';
          this.memberEligibility = null;
          this.openSearchMemberModal(); // Ouvrir le modal pour afficher le message d'erreur
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
      { header: 'Téléphone1', dataKey: 'phone1' },
      { header: 'Téléphone2', dataKey: 'phone2' },
      { header: 'Date adhésion', dataKey: 'membershipDate' },
      { header: 'Effectif', dataKey: 'workforce' },
      { header: 'Titre', dataKey: 'title' },
      { header: 'Trimestre DBR', dataKey: 'dbrTrimester' },
      { header: 'Année DBR', dataKey: 'dbrYear' },
      { header: 'Trimestre DTR', dataKey: 'dtrTrimester' },
      { header: 'Année DTR', dataKey: 'dtrYear' },
    ];

    // Définir les données à inclure explicitement
    const rows = this.members.map((member) => [
      member.memberNumber,
      member.type,
      member.companyName,
      member.address1,
      member.address2,
      member.city,
      member.phone1,
      member.phone2,
      member.membershipDate,
      member.workforce,
      member.title,
      member.dbrTrimester,
      member.dbrYear,
      member.dtrTrimester,
      member.dtrYear,
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

    doc.save('liste_adhérents.pdf');
  }

  exportCSV(): void {
    const headers = [
      'N° Adhérent',
      'Type',
      'Raison sociale',
      'Adresse 1',
      'Adresse 2',
      'Ville',
      'Téléphone 1',
      'Téléphone 2',
      'Date adhésion',
      'Effectif',
      'Titre',
      'Trimestre DBR',
      'Année DBR',
      'Trimestre DTR',
      'Année DTR',
    ];

    // Créer les lignes du CSV
    const rows = this.members.map((member) => [
      member.memberNumber,
      member.type,
      member.companyName,
      member.address1,
      member.address2,
      member.city,
      member.phone1,
      member.phone2,
      member.membershipDate,
      member.workforce,
      member.title,
      member.dbrTrimester,
      member.dbrYear,
      member.dtrTrimester,
      member.dtrYear,
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
    link.setAttribute('download', 'liste_adhérents.csv');
    link.style.visibility = 'hidden';

    // Ajouter le lien à la page, le cliquer, puis le supprimer
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  }

  resetForm(): void {
    this.editForm.reset();
    this.selectedMember = null;
  }
}
