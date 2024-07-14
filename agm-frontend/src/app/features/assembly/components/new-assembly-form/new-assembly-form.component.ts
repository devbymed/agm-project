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
import { ToastrService } from "ngx-toastr";

interface SelectOption {
  value: string;
  label: string;
}

@Component({
  selector: 'app-new-assembly-form',
  standalone: true,
  imports: [ReactiveFormsModule, InputComponent, SelectComponent, ButtonComponent],
  templateUrl: './new-assembly-form.component.html',
  styles: ``
})
export class NewAssemblyFormComponent implements OnInit {
  newAssemblyForm: FormGroup;
  types: SelectOption[] = [];
  years: SelectOption[] = [];
  cities: SelectOption[] = [];

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

  constructor(private fb: FormBuilder, private router: Router, private assemblyService: AssemblyService, private assemblyStateService: AssemblyStateService, private toastr: ToastrService
  ) { }

  ngOnInit(): void {
    this.newAssemblyForm = this.fb.group({
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
    this.newAssemblyForm.markAllAsTouched();
    if (this.newAssemblyForm.valid) {
      const formData = new FormData();
      Object.keys(this.newAssemblyForm.controls).forEach(key => {
        const control = this.newAssemblyForm.get(key);
        if (control && control.value) {
          formData.append(key, control.value);
        }
      });

      if (this.routeSheetFile) formData.append('routeSheet', this.routeSheetFile);
      if (this.invitationLetterFile) formData.append('invitationLetter', this.invitationLetterFile);
      if (this.attendanceSheetFile) formData.append('attendanceSheet', this.attendanceSheetFile);
      if (this.proxyFile) formData.append('proxy', this.proxyFile);
      if (this.attendanceFormFile) formData.append('attendanceForm', this.attendanceFormFile);

      this.assemblyService.createAssembly(formData).subscribe({
        next: (response: ApiResponse<Assembly>) => {
          this.resetForm();
          this.toastr.success(response.message);
          this.assemblyStateService.updateCurrentAssemblyState(true);
          this.router.navigate(['/preparation-assemblee/nouvelle-assemblee/assemblee-en-cours']);
        },
        error: (error) => {
          console.error('Une erreur est survenue lors de la création de l\'assemblée', error);
        }
      });
    }
  }

  resetForm() {
    this.newAssemblyForm.reset({
      type: '',
      year: '',
      day: '',
      time: '',
      address: '',
      city: ''
    });
    Object.keys(this.newAssemblyForm.controls).forEach(key => {
      this.newAssemblyForm.get(key)?.setErrors(null);
    });

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
