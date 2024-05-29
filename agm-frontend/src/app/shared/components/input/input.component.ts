import { NgClass } from '@angular/common';
import { Component, Input, ViewChild, booleanAttribute, forwardRef } from '@angular/core';
import {
  AbstractControl,
  NG_VALUE_ACCESSOR,
  ValidationErrors,
  ValidatorFn,
  Validators,
} from '@angular/forms';
import { BaseControlValueAccessorService } from '@core/services/base-control-value-accessor.service';
import { ValidationErrorService } from '@core/services/validation-error.service';
import { InputTypeDirective } from '@shared/directives/input-type.directive';

export function emailDomainValidator(domain: string): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const value: string = control.value || '';
    const emailDomain = value.split('@')[1];

    if (emailDomain !== domain) {
      return { emailDomain: true };
    }

    return null;
  };
}

@Component({
  selector: 'app-input',
  standalone: true,
  imports: [NgClass, InputTypeDirective],
  templateUrl: './input.component.html',
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => InputComponent),
      multi: true,
    },
  ],
})
export class InputComponent extends BaseControlValueAccessorService<string> {
  protected readonly Validators = Validators;
  isPasswordVisible = false;

  @Input({ required: true }) label: string;
  @Input({ required: true }) id: string;
  @Input({ required: true }) type: string;
  @Input({ required: true }) name: string;
  @Input() placeholder: string = '';
  @Input({ transform: booleanAttribute }) required = false;
  @Input({ transform: booleanAttribute }) showRequiredIndicator = false;
  @ViewChild(InputTypeDirective) inputTypeDirective: InputTypeDirective;

  constructor(public validationErrorService: ValidationErrorService) {
    super();
  }

  togglePasswordVisibility(): void {
    this.isPasswordVisible = !this.isPasswordVisible;
    this.inputTypeDirective.togglePasswordVisibility();
  }
}
