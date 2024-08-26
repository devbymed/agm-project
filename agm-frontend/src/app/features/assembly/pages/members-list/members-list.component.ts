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

  resetForm(): void {
    this.editForm.reset();
    this.selectedMember = null;
  }
}
