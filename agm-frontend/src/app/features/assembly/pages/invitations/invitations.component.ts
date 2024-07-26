import { NgClass } from "@angular/common";
import { Component, OnInit } from '@angular/core';
import { InvitationGenerationComponent } from "@features/assembly/components/invitation-generation/invitation-generation.component";
import { InvitationReportsComponent } from "@features/assembly/components/invitation-reports/invitation-reports.component";
import { MemberAllocationComponent } from "@features/assembly/components/member-allocation/member-allocation.component";
import { ViewAssignmentsComponent } from "@features/assembly/components/view-assignments/view-assignments.component";
import { initFlowbite } from "flowbite";

@Component({
  selector: 'app-invitations',
  standalone: true,
  imports: [NgClass, MemberAllocationComponent, InvitationGenerationComponent, ViewAssignmentsComponent, InvitationReportsComponent],
  templateUrl: './invitations.component.html',
})
export class InvitationsComponent implements OnInit {

  constructor() { }

  ngOnInit(): void {
    initFlowbite();
  }
}
