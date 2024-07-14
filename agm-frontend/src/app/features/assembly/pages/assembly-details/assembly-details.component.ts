import { DatePipe, NgIf } from "@angular/common";
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms";
import { Router } from "@angular/router";
import { AssemblyStateService } from "@features/assembly/services/assembly-state.service";
import { AssemblyService } from "@features/assembly/services/assembly.service";
import { ButtonComponent } from "@shared/components/button/button.component";
import { InputComponent } from "@shared/components/form/input/input.component";
import { SelectComponent } from "@shared/components/form/select/select.component";
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

  private destroy$ = new Subject<void>();

  constructor(private fb: FormBuilder, private router: Router, private assemblyService: AssemblyService, private assemblyStateService: AssemblyStateService, private toastr: ToastrService
  ) { }

  ngOnInit(): void {
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
      });

    this.assemblyStateService.checkCurrentAssembly();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
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
}
