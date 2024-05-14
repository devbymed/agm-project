import { NgClass, NgIf } from '@angular/common';
import { Component, Input, booleanAttribute, forwardRef } from '@angular/core';
import {
  AbstractControl,
  NG_VALUE_ACCESSOR,
  ValidationErrors,
  ValidatorFn,
  Validators,
} from '@angular/forms';
import { BaseControlValueAccessorService } from '../../../../core/services/base-control-value-accessor.service';
import { InputTypeDirective } from '../../../directives/input-type.directive';

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
  imports: [NgClass, NgIf, InputTypeDirective],
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
  @Input({ required: true }) label: string;
  @Input({ required: true }) id: string;
  @Input({ required: true }) type: string;
  @Input({ required: true }) name: string;
  @Input({ transform: booleanAttribute }) required = false;
  @Input() placeholder: string = '';

  getErrorMessage(): string {
    if (this.control.errors?.['required']) return 'Ce champ est obligatoire';
    if (this.control.errors?.['email'] || this.control.errors?.['emailDomain'])
      return 'Adresse email invalide';
    if (this.control.errors?.['minlength'])
      return 'Ce champ doit contenir au moins 8 caractères';
    if (this.control.errors?.['maxlength'])
      return 'Ce champ doit contenir au maximum 50 caractères';
    return '';
  }
}
