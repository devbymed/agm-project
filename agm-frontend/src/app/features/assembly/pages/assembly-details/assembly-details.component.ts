import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms";
import { Router } from "@angular/router";
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
  selector: 'app-assembly-details',
  standalone: true,
  imports: [ReactiveFormsModule, InputComponent, SelectComponent, ButtonComponent],
  templateUrl: './assembly-details.component.html',
})
export class AssemblyDetailsComponent implements OnInit {
  assemblyDetailsForm: FormGroup;
  types: SelectOption[] = [];
  years: SelectOption[] = [];
  cities: SelectOption[] = [];

  constructor(private fb: FormBuilder, private router: Router, private assemblyService: AssemblyService, private toastr: ToastrService
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
  }

  deleteCurrentAssembly(): void {
    this.assemblyService.deleteCurrentAssembly().subscribe((response) => {
      if (response.status === 'OK') {
        this.toastr.success(response.message);
        setTimeout(() => {
          window.location.reload();
        }, 3000);
      } else {
        console.error(response.message);
      }
    });
  }
}
