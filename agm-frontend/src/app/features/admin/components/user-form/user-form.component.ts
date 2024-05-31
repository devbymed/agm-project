import { Component, inject } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms";
import { ButtonComponent } from "@shared/components/button/button.component";
import { InputComponent } from "@shared/components/input/input.component";
import { SelectComponent } from "@shared/components/select/select.component";

@Component({
  selector: 'app-user-form',
  standalone: true,
  imports: [InputComponent, SelectComponent, ButtonComponent, ReactiveFormsModule],
  templateUrl: './user-form.component.html',
  styles: ``
})
export class UserFormComponent {
  private fb = inject(FormBuilder);
  userForm: FormGroup;
  profiles = [
    { value: 'gestionnaire', label: 'Gestionnaire' },
    { value: 'agent_de_relance', label: 'Agent de relance' },
    { value: 'Consultant', label: 'Consultant' },
  ];

  constructor() {
    this.initializeForm();
  }

  private initializeForm(): void {
    this.userForm = this.fb.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      profile: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
    });
  }
}
