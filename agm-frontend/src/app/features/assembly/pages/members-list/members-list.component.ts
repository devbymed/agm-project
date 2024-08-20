import { NgFor, NgIf } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { ApiResponse } from '@core/models/api-response.model';
import { Member } from '@features/assembly/models/member.model';
import { MemberService } from '@features/assembly/services/member.service';
import { ButtonComponent } from '@shared/components/button/button.component';
import { InputComponent } from '@shared/components/form/input/input.component';
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
  }

  ngOnInit(): void {
    this.loadMembers();
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
    this.selectedMember = member;
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

  resetForm(): void {
    this.editForm.reset();
    this.selectedMember = null;
  }
}
