import { NgFor, NgIf } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { ApiResponse } from '@core/models/api-response.model';
import { Reason } from '@features/admin/models/reason';
import { ReasonService } from '@features/admin/services/reason.service';
import { ButtonComponent } from '@shared/components/button/button.component';
import { InputComponent } from '@shared/components/form/input/input.component';
import { Modal } from 'flowbite';
import { ToastrService } from 'ngx-toastr';
import { Subject, takeUntil } from 'rxjs';

@Component({
  selector: 'app-settings',
  standalone: true,
  imports: [ReactiveFormsModule, NgIf, NgFor, InputComponent, ButtonComponent],
  templateUrl: './settings.component.html',
})
export class SettingsComponent implements OnInit {
  reasons: Reason[] = [];
  addReasonForm: FormGroup;
  updateReasonForm: FormGroup;
  selectedReason: Reason | null = null;
  addReasonModal: Modal | null = null;
  editReasonModal: Modal | null = null;
  private destroy$ = new Subject<void>();

  constructor(
    private fb: FormBuilder,
    private reasonService: ReasonService,
    private toastr: ToastrService,
  ) {
    this.addReasonForm = this.fb.group({
      description: ['', Validators.required],
    });

    this.updateReasonForm = this.fb.group({
      description: ['', Validators.required],
    });
  }

  ngOnInit(): void {
    this.initModals();
    this.loadReasons();
  }

  loadReasons(): void {
    this.reasonService
      .getReasons()
      .subscribe((response: ApiResponse<Reason[]>) => {
        this.reasons = response.data || [];
      });
  }

  initModals(): void {
    const editReasonModalElement = document.getElementById('editReasonModal');
    if (editReasonModalElement) {
      this.editReasonModal = new Modal(editReasonModalElement);
    }

    const addReasonModalElement = document.getElementById('addReasonModal');
    if (addReasonModalElement) {
      this.addReasonModal = new Modal(addReasonModalElement);
    }
  }

  openAddReasonModal(): void {
    if (this.addReasonModal) {
      this.addReasonModal.show();
    }
  }

  openEditReasonModal(reason: Reason): void {
    this.selectedReason = reason;
    this.updateReasonForm.patchValue({
      id: reason.id,
      description: reason.description,
    });
    if (this.editReasonModal) {
      this.editReasonModal.show();
    }
  }

  closeAddReasonModal(): void {
    if (this.addReasonModal) {
      this.addReasonModal.hide();
    }
    this.addReasonForm.reset();
  }

  closeEditReasonModal(): void {
    if (this.editReasonModal) {
      this.editReasonModal.hide();
    }
    this.selectedReason = null;
  }

  onSubmit(): void {
    if (this.addReasonForm.valid) {
      const newReason: Reason = this.addReasonForm.value;
      this.reasonService.createReason(newReason).subscribe(() => {
        this.loadReasons();
        this.addReasonForm.reset();
        if (this.addReasonModal) {
          this.addReasonModal.hide();
        }
      });
    }
  }

  updateReason(): void {
    if (this.selectedReason && this.updateReasonForm.valid) {
      const updatedReason: Reason = this.updateReasonForm.value;
      this.reasonService
        .updateReason(this.selectedReason.id, updatedReason)
        .pipe(takeUntil(this.destroy$))
        .subscribe({
          next: (response: ApiResponse<Reason[]>) => {
            this.toastr.success(response.message);
            this.loadReasons();
            if (this.editReasonModal) {
              this.editReasonModal.hide();
            }
          },
          error: () => {
            this.toastr.error('Une erreur est survenue lors de la mise à jour');
          },
        });
    }
  }

  deleteReason(id: number): void {
    this.reasonService.deleteReason(id).subscribe((response) => {
      this.toastr.success(response.message);
      this.loadReasons();
    });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
