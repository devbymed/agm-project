import {
  DatePipe,
  DecimalPipe,
  NgClass,
  NgFor,
  NgIf,
  NgStyle,
  PercentPipe,
} from '@angular/common';
import { Component, OnDestroy, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { ApiResponse } from '@core/models/api-response.model';
import { Action } from '@features/assembly/models/action.model';
import { FileSizePipe } from '@features/assembly/pipes/file-size.pipe';
import { ActionService } from '@features/assembly/services/action.service';
import { AssemblyStateService } from '@features/assembly/services/assembly-state.service';
import { InputComponent } from '@shared/components/form/input/input.component';
import { SelectComponent } from '@shared/components/form/select/select.component';
import { TextareaComponent } from '@shared/components/textarea/textarea.component';
import { Modal } from 'flowbite';
import { ToastrService } from 'ngx-toastr';
import { Subject, takeUntil } from 'rxjs';

interface SelectOption {
  value: string;
  label: string;
}

@Component({
  selector: 'app-fdr-follow-up',
  standalone: true,
  imports: [
    NgIf,
    NgFor,
    NgStyle,
    NgClass,
    DatePipe,
    PercentPipe,
    DecimalPipe,
    FileSizePipe,
    InputComponent,
    SelectComponent,
    TextareaComponent,
    ReactiveFormsModule,
  ],
  templateUrl: './fdr-follow-up.component.html',
})
export class FdrFollowUpComponent implements OnInit, OnDestroy {
  actions: Action[] = [];
  isFiltered: boolean = false;
  updateActionForm: FormGroup;
  currentAssemblyDetails: any = null;
  editActionModal: Modal | null = null;
  actionDetailsModal: Modal | null = null;
  selectedAction: Action | null = null;
  dropdownOpen: boolean[] = [];
  private destroy$ = new Subject<void>();

  progressStatusOptions: SelectOption[] = [
    { value: '0', label: '0%' },
    { value: '10', label: '10%' },
    { value: '20', label: '20%' },
    { value: '30', label: '30%' },
    { value: '40', label: '40%' },
    { value: '50', label: '50%' },
    { value: '60', label: '60%' },
    { value: '70', label: '70%' },
    { value: '80', label: '80%' },
    { value: '90', label: '90%' },
    { value: '100', label: '100%' },
  ];

  constructor(
    private fb: FormBuilder,
    private assemblyStateService: AssemblyStateService,
    private actionService: ActionService,
    private toastr: ToastrService,
  ) {}

  ngOnInit() {
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

  toggleDropdown(index: number): void {
    // Si le dropdown à cet index est déjà ouvert, le fermer
    if (this.dropdownOpen[index]) {
      this.dropdownOpen[index] = false;
    } else {
      // Sinon, fermer tous les autres dropdowns et ouvrir celui-ci
      this.closeAllDropdowns();
      this.dropdownOpen[index] = true;
    }
  }

  closeAllDropdowns(): void {
    this.dropdownOpen = this.dropdownOpen.map(() => false);
  }

  initModals(): void {
    const editActionModalElement = document.getElementById('editActionModal');
    if (editActionModalElement) {
      this.editActionModal = new Modal(editActionModalElement);
    }

    const actionDetailsModalElement =
      document.getElementById('actionDetailsModal');
    if (actionDetailsModalElement) {
      this.actionDetailsModal = new Modal(actionDetailsModalElement);
    }
  }

  openEditActionModal(action: Action): void {
    this.closeAllDropdowns(); // Fermer les dropdowns
    this.selectedAction = action;
    this.updateActionForm.patchValue({
      id: action.id,
      name: action.name,
      entity: action.entity || '',
      responsible: action.responsible || '',
      startDate: action.startDate || '',
      endDate: action.endDate || '',
      deliverable: action.deliverable || '',
      progressStatus: action.progressStatus > 0 ? action.progressStatus : '',
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
      this.updateActionForm.reset();
    }
    this.selectedAction = null;
  }

  openActionDetailsModal(action: Action): void {
    this.closeAllDropdowns(); // Fermer les dropdowns
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
    this.actionService
      .getActions()
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (response: ApiResponse<Action[]>) => {
          this.actions = response.data || [];
        },
        error: (error) => {
          console.error('Error loading actions:', error);
        },
      });
  }

  onFilterOverdueUnclosed(event: Event): void {
    const isChecked = (event.target as HTMLInputElement).checked;

    if (isChecked) {
      this.actionService.getOverdueUnclosedActions().subscribe({
        next: (response: ApiResponse<Action[]>) => {
          this.actions = response.data || [];
          this.isFiltered = true; // Indiquer que les actions sont filtrées
        },
        error: (error) => {
          console.error('Error fetching overdue actions:', error);
        },
      });
    } else {
      this.loadActions();
      this.isFiltered = false;
    }
  }

  onUpdateAction(): void {
    console.log('Updating action:', this.updateActionForm.value);
    this.updateActionForm.markAllAsTouched();

    if (this.updateActionForm.valid && this.selectedAction) {
      const formData = new FormData();

      // Ajouter les valeurs du formulaire au FormData
      Object.keys(this.updateActionForm.value).forEach((key) => {
        formData.append(key, this.updateActionForm.get(key)?.value);
      });

      // Ajouter les fichiers au FormData
      const fileInput = document.getElementById(
        'actionAttachment',
      ) as HTMLInputElement;
      if (fileInput && fileInput.files && fileInput.files.length > 0) {
        for (let i = 0; i < fileInput.files.length; i++) {
          formData.append('attachments', fileInput.files[i]);
        }
      }

      // Envoyer le FormData via le service
      this.actionService
        .updateAction(this.selectedAction.id, formData)
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
          },
        });
    }
  }

  onDownloadAttachment(actionId: number, fileName: string): void {
    this.actionService.downloadAttachment(actionId, fileName);
  }

  getFileType(fileType: string): string {
    return this.actionService.getReadableFileType(fileType);
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
