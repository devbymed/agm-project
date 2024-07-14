import { DatePipe, NgIf, NgStyle } from "@angular/common";
import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms";
import { AssemblyStateService } from "@features/assembly/services/assembly-state.service";
import { InputComponent } from "@shared/components/form/input/input.component";
import { initFlowbite } from "flowbite";
import { ToastrService } from "ngx-toastr";
import { Subject, takeUntil } from "rxjs";

@Component({
  selector: 'app-fdr-follow-up',
  standalone: true,
  imports: [NgIf, NgStyle, DatePipe, InputComponent, ReactiveFormsModule],
  templateUrl: './fdr-follow-up.component.html',
})
export class FdrFollowUpComponent implements OnInit, OnDestroy {
  updateActionForm: FormGroup;
  currentAssemblyDetails: any = null;
  private destroy$ = new Subject<void>();

  constructor(private fb: FormBuilder, private assemblyStateService: AssemblyStateService, private toastr: ToastrService
  ) { }

  ngOnInit() {
    initFlowbite();
    this.updateActionForm = this.fb.group({
      description: ['', Validators.required],
      entity: ['', Validators.required],
      responsible: ['', Validators.required],
      startDate: ['', Validators.required],
      endDate: ['', Validators.required],
      deliverable: ['', Validators.required],
      progressStatus: ['', Validators.required],
      realizationDate: ['', Validators.required],
      observation: ['', Validators.required],
    });

    this.assemblyStateService.currentAssemblyDetails$
      .pipe(takeUntil(this.destroy$))
      .subscribe((details: any) => {
        this.currentAssemblyDetails = details;
      });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
