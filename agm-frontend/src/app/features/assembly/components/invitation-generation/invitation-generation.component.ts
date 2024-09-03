import { NgFor, NgIf } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { Member } from '@features/assembly/models/member.model';
import { MemberService } from '@features/assembly/services/member.service';
import { ButtonComponent } from '@shared/components/button/button.component';
import { InputComponent } from '@shared/components/form/input/input.component';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-invitation-generation',
  standalone: true,
  imports: [NgIf, NgFor, ReactiveFormsModule, InputComponent, ButtonComponent],
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
