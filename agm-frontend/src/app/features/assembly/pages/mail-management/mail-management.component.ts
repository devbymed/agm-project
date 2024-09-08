import { CommonModule, DatePipe } from '@angular/common';
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
import { ReturnedMail } from '@features/assembly/models/returned-mail';
import { ReturnedMailService } from '@features/assembly/services/returned-mail.service';
import { AlertComponent } from '@shared/components/alert/alert.component';
import { ButtonComponent } from '@shared/components/button/button.component';
import { InputComponent } from '@shared/components/form/input/input.component';
import { SelectComponent } from '@shared/components/form/select/select.component';
import { Modal } from 'flowbite';
import { ToastrService } from 'ngx-toastr';

interface SelectOption {
  value: string;
  label: string;
}

@Component({
  selector: 'app-mail-management',
  standalone: true,
  imports: [
    CommonModule,
    DatePipe,
    ReactiveFormsModule,
    InputComponent,
    SelectComponent,
    ButtonComponent,
    AlertComponent,
  ],
  templateUrl: './mail-management.component.html',
})
export class MailManagementComponent implements OnInit {
  returnedMails: ReturnedMail[] = [];
  reasons: SelectOption[] = [];
  addReturnedMailForm: FormGroup;
  addReturnedMailModal: Modal | null = null;
  errorMessage: string | null = null;

  constructor(
    private fb: FormBuilder,
    private returnedMailService: ReturnedMailService,
    private reasonService: ReasonService,
    private toastr: ToastrService,
  ) {
    this.addReturnedMailForm = this.fb.group({
      memberNumber: ['', Validators.required],
      returnDate: ['', Validators.required],
      reasonId: ['', Validators.required],
    });
  }

  ngOnInit(): void {
    this.initModals();
    this.loadReasons();
    this.loadReturnedMails();
  }

  initModals() {
    const addReturnedMailModalElement = document.getElementById(
      'addReturnedMailModal',
    );
    if (addReturnedMailModalElement) {
      this.addReturnedMailModal = new Modal(addReturnedMailModalElement);
    }
  }

  loadReturnedMails() {
    this.returnedMailService
      .getReturnedMails()
      .subscribe(
        (response: ApiResponse<ReturnedMail[]>) =>
          (this.returnedMails = response.data || []),
      );
  }

  loadReasons() {
    this.reasonService.getReasons().subscribe({
      next: (response) => {
        const apiResponse = response as ApiResponse<Reason[]>;
        console.log('reasons', apiResponse.data);
        this.reasons = (apiResponse.data || []).map((reason) => ({
          value: reason.id.toString(),
          label: reason.description,
        }));
      },
      error: (error) => {
        console.error('Erreur lors de la récupération des raisons', error);
      },
    });
  }

  openAddReturnedMailModal(): void {
    if (this.addReturnedMailModal) {
      this.addReturnedMailModal.show();
    }
  }

  closeReturnedMailModal(): void {
    if (this.addReturnedMailModal) {
      this.addReturnedMailModal.hide();
    }
  }

  onSubmit(): void {
    this.addReturnedMailForm.markAllAsTouched();
    if (this.addReturnedMailForm.valid) {
      const newReturnedMail = {
        memberNumber: this.addReturnedMailForm.value.memberNumber,
        returnDate: this.addReturnedMailForm.value.returnDate,
        reasonId: this.addReturnedMailForm.value.reasonId,
      };

      this.returnedMailService.createReturnedMail(newReturnedMail).subscribe({
        next: (response) => {
          if (response.status === 'OK') {
            this.toastr.success(response.message);
            this.errorMessage = null;
            this.loadReturnedMails();
            this.closeReturnedMailModal();
            this.addReturnedMailForm.reset({
              memberNumber: '',
              returnDate: '',
              reasonId: '',
            });
          } else {
            this.toastr.error("Erreur lors de l'ajout du retour de courrier.");
          }
        },
        error: (error) => {
          if (error?.error?.message) {
            this.errorMessage = error.error.message;
          } else {
            this.errorMessage =
              "Une erreur s'est produite lors de la création du retour de courrier.";
          }
          console.error(
            'Erreur lors de la création du retour de courrier',
            error,
          );
        },
      });
    } else {
      this.toastr.warning('Veuillez remplir tous les champs obligatoires.');
    }
  }
}
