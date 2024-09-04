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

  reset(): void {
    this.form.reset({
      agent: '',
    });
    this.selectedMemberNumbers = [];
  }
}
