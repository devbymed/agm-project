import { DatePipe, NgClass, NgFor, NgIf, NgStyle, PercentPipe } from "@angular/common";
import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms";
import { ApiResponse } from "@core/models/api-response.model";
import { Action } from "@features/assembly/models/action.model";
import { ActionService } from "@features/assembly/services/action.service";
import { AssemblyStateService } from "@features/assembly/services/assembly-state.service";
import { InputComponent } from "@shared/components/form/input/input.component";
import { Modal, initFlowbite } from "flowbite";
import { ToastrService } from "ngx-toastr";
import { Subject, takeUntil } from "rxjs";

@Component({
  selector: 'app-fdr-follow-up',
  standalone: true,
  imports: [NgIf, NgFor, NgStyle, NgClass, DatePipe, PercentPipe, InputComponent, ReactiveFormsModule],
  templateUrl: './fdr-follow-up.component.html',
})
export class FdrFollowUpComponent implements OnInit, OnDestroy {
  actions: Action[] = [];
  updateActionForm: FormGroup;
  currentAssemblyDetails: any = null;
  editActionModal: Modal | null = null;
  actionDetailsModal: Modal | null = null;
  selectedAction: Action | null = null;
  private destroy$ = new Subject<void>();

  constructor(private fb: FormBuilder, private assemblyStateService: AssemblyStateService, private actionService: ActionService, private toastr: ToastrService
  ) { }

  ngOnInit() {
    initFlowbite();
    this.initModals();
    this.updateActionForm = this.fb.group({
      name: [''],
      entity: ['', Validators.required],
      responsible: ['', Validators.required],
      startDate: ['', Validators.required],
      endDate: ['', Validators.required],
      deliverable: [''],
      progressStatus: ['', Validators.required],
      realizationDate: [''],
      observation: [''],
    });

    this.loadActions();

    this.assemblyStateService.currentAssemblyDetails$
      .pipe(takeUntil(this.destroy$))
      .subscribe((details: any) => {
        this.currentAssemblyDetails = details;
      });
  }

  initModals(): void {
    const editActionModalElement = document.getElementById('editActionModal');
    if (editActionModalElement) {
      this.editActionModal = new Modal(editActionModalElement);
    }

    const actionDetailsModalElement = document.getElementById('actionDetailsModal');
    if (actionDetailsModalElement) {
      this.actionDetailsModal = new Modal(actionDetailsModalElement);
    }
  }

  openEditActionModal(action: Action): void {
    this.selectedAction = action;
    this.updateActionForm.patchValue({
      id: action.id,
      name: action.name,
      entity: action.entity || '',
      responsible: action.responsible || '',
      startDate: action.startDate || '',
      endDate: action.endDate || '',
      deliverable: action.deliverable || '',
      progressStatus: action.progressStatus || '',
      realizationDate: action.realizationDate || '',
      observation: action.observation || '',
    });
    if (this.editActionModal) {
      this.editActionModal.show();
    }
  }

  closeEditActionModal(): void {
    if (this.editActionModal) {
      this.editActionModal.hide();
    }
    this.selectedAction = null;
  }

  openActionDetailsModal(action: Action): void {
    this.selectedAction = action;
    if (this.actionDetailsModal) {
      this.actionDetailsModal.show();
    }
  }

  closeActionDetailsModal(): void {
    if (this.actionDetailsModal) {
      this.actionDetailsModal.hide();
    }
    this.selectedAction = null;
  }

  loadActions(): void {
    this.actionService.getActions()
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (response: ApiResponse<Action[]>) => {
          this.actions = response.data || [];
        },
        error: (error) => {
          console.error('Error loading actions:', error);
        }
      });
  }

  onUpdateAction(): void {
    console.log('Updating action:', this.updateActionForm.value);
    if (this.updateActionForm.valid && this.selectedAction) {
      this.actionService.updateAction(this.selectedAction.id, this.updateActionForm.value)
        .pipe(takeUntil(this.destroy$))
        .subscribe({
          next: (response: ApiResponse<Action>) => {
            this.toastr.success(response.message);
            this.loadActions();
            if (this.editActionModal) {
              this.editActionModal.hide();
            }
          },
          error: (error) => {
            console.error('Error updating action:', error);
          }
        });
    }
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
