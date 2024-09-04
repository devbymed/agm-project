import { CommonModule, DatePipe } from '@angular/common';
import { Component } from '@angular/core';
import { Member } from '@features/assembly/models/member.model';
import { MemberService } from '@features/assembly/services/member.service';
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
}
