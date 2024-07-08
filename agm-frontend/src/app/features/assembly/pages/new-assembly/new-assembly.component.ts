import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms";
import { InputComponent } from "@shared/components/form/input/input.component";

@Component({
  selector: 'app-new-assembly',
  standalone: true,
  imports: [ReactiveFormsModule, InputComponent],
  templateUrl: './new-assembly.component.html',
})
export class NewAssemblyComponent {
  newAssemblyForm: FormGroup;

  constructor(
    private fb: FormBuilder
  ) {
    this.newAssemblyForm = this.fb.group({
      type: ['', Validators.required],
      year: ['', [Validators.required, Validators.min(new Date().getFullYear()), Validators.max(new Date().getFullYear() + 1)]],
    });
  }
}