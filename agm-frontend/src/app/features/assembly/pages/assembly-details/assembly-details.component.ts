import { DatePipe, NgIf } from "@angular/common";
import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms";
import { Router } from "@angular/router";
import { ApiResponse } from "@core/models/api-response.model";
import { Assembly } from "@features/assembly/models/assembly.model";
import { AssemblyStateService } from "@features/assembly/services/assembly-state.service";
import { AssemblyService } from "@features/assembly/services/assembly.service";
import { ButtonComponent } from "@shared/components/button/button.component";
import { InputComponent } from "@shared/components/form/input/input.component";
import { SelectComponent } from "@shared/components/form/select/select.component";
import { initFlowbite } from "flowbite";
import { ToastrService } from "ngx-toastr";
import { Subject, takeUntil } from "rxjs";

interface SelectOption {
  value: string;
  label: string;
}

@Component({
  selector: 'app-assembly-details',
  standalone: true,
  imports: [ReactiveFormsModule, NgIf, DatePipe, InputComponent, SelectComponent, ButtonComponent],
  templateUrl: './assembly-details.component.html',
})
export class AssemblyDetailsComponent implements OnInit {
  assemblyDetailsForm: FormGroup;
  types: SelectOption[] = [];
  years: SelectOption[] = [];
  cities: SelectOption[] = [];
  currentAssemblyDetails: any = null;

  @ViewChild('routeSheetInput') routeSheetInput: any;
  @ViewChild('invitationLetterInput') invitationLetterInput: any;
  @ViewChild('attendanceSheetInput') attendanceSheetInput: any;
  @ViewChild('proxyInput') proxyInput: any;
  @ViewChild('attendanceFormInput') attendanceFormInput: any;

  routeSheetFile: File | null = null;
  invitationLetterFile: File | null = null;
  attendanceSheetFile: File | null = null;
  proxyFile: File | null = null;
  attendanceFormFile: File | null = null;

  private destroy$ = new Subject<void>();

  constructor(private fb: FormBuilder, private router: Router, private assemblyService: AssemblyService, private assemblyStateService: AssemblyStateService, private toastr: ToastrService
  ) { }

  ngOnInit(): void {
    initFlowbite();
    this.assemblyDetailsForm = this.fb.group({
      type: ['', Validators.required],
      year: ['', Validators.required],
      day: ['', Validators.required],
      time: ['', Validators.required],
      address: ['', [Validators.required, Validators.maxLength(255)]],
      city: ['', Validators.required],
    });

    this.types = [
      { value: 'AGO', label: 'AGO' },
      { value: 'AGE', label: 'AGE' },
      { value: 'AGM', label: 'AGM' }
    ];

    const currentYear = new Date().getFullYear();
    this.years = [
      { value: currentYear.toString(), label: currentYear.toString() },
      { value: (currentYear + 1).toString(), label: (currentYear + 1).toString() }
    ];

    this.cities = [
      { value: 'Casablanca', label: 'Casablanca' },
    ];

    this.assemblyStateService.currentAssemblyDetails$
      .pipe(takeUntil(this.destroy$))
      .subscribe((details: any) => {
        this.currentAssemblyDetails = details;
        if (details) {
          this.assemblyDetailsForm.patchValue(details);
        }
      });

    this.assemblyStateService.checkCurrentAssembly();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  onFileChange(event: Event, field: string) {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      const file = input.files[0];
      switch (field) {
        case 'routeSheet':
          this.routeSheetFile = file;
          break;
        case 'invitationLetter':
          this.invitationLetterFile = file;
          break;
        case 'attendanceSheet':
          this.attendanceSheetFile = file;
          break;
        case 'proxy':
          this.proxyFile = file;
          break;
        case 'attendanceForm':
          this.attendanceFormFile = file;
          break;
      }
    }
  }

  onSubmit() {
    this.assemblyDetailsForm.markAllAsTouched();
    if (this.assemblyDetailsForm.valid) {
      const formData = new FormData();
      Object.keys(this.assemblyDetailsForm.controls).forEach(key => {
        const control = this.assemblyDetailsForm.get(key);
        if (control && control.value) {
          formData.append(key, control.value);
        }
      });

      if (this.routeSheetFile) formData.append('routeSheet', this.routeSheetFile);
      if (this.invitationLetterFile) formData.append('invitationLetter', this.invitationLetterFile);
      if (this.attendanceSheetFile) formData.append('attendanceSheet', this.attendanceSheetFile);
      if (this.proxyFile) formData.append('proxy', this.proxyFile);
      if (this.attendanceFormFile) formData.append('attendanceForm', this.attendanceFormFile);

      this.assemblyService.updateCurrentAssembly(formData).subscribe({
        next: (response: ApiResponse<Assembly>) => {
          this.toastr.success(response.message);
          this.assemblyStateService.updateCurrentAssemblyDetails(response.data); // Update details here
          this.assemblyStateService.updateCurrentAssemblyState(true);
          this.router.navigate(['/preparation-assemblee/nouvelle-assemblee/assemblee-en-cours']);
          this.resetForm();
        },
        error: (error) => {
          console.error('Une erreur est survenue lors de la modification de l\'assemblée', error);
        }
      });
    }
  }

  closeCurrentAssembly() {
    this.assemblyService.closeCurrentAssembly().subscribe({
      next: (response) => {
        this.toastr.success(response.message);
        this.assemblyStateService.updateCurrentAssemblyState(false);
        this.currentAssemblyDetails = null;
      },
      error: (error) => {
        console.error('Error closing assembly:', error);
      }
    });
  }

  deleteCurrentAssembly(): void {
    this.assemblyService.deleteCurrentAssembly()
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (response) => {
          if (response.status === 'OK') {
            this.toastr.success(response.message);
            this.assemblyStateService.updateCurrentAssemblyState(false);
            this.router.navigate(['/preparation-assemblee/nouvelle-assemblee']);
          } else {
            console.error(response.message);
          }
        },
        error: (error) => {
          console.error('Error deleting assembly:', error);
        }
      });
  }

  resetForm() {
    if (this.currentAssemblyDetails) {
      this.assemblyDetailsForm.patchValue(this.currentAssemblyDetails);
    }

    this.routeSheetInput.nativeElement.value = '';
    this.invitationLetterInput.nativeElement.value = '';
    this.attendanceSheetInput.nativeElement.value = '';
    this.proxyInput.nativeElement.value = '';
    this.attendanceFormInput.nativeElement.value = '';

    this.routeSheetFile = null;
    this.invitationLetterFile = null;
    this.attendanceSheetFile = null;
    this.proxyFile = null;
    this.attendanceFormFile = null;
  }
}
