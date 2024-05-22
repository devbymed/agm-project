import { NgClass, NgIf } from '@angular/common';
import { Component, Input, booleanAttribute, forwardRef } from '@angular/core';
import {
  AbstractControl,
  NG_VALUE_ACCESSOR,
  ValidationErrors,
  ValidatorFn,
  Validators,
} from '@angular/forms';
import { BaseControlValueAccessorService } from '@core/services/base-control-value-accessor.service';
import { InputTypeDirective } from '@shared/directives/input-type.directive';
import { PasswordStrengthIndicatorComponent } from '../../core/auth/password-strength-indicator.component';

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
  template: `
    <label
      [for]="id"
      class="mb-2 block text-sm font-medium text-gray-500"
      [ngClass]="{ 'text-red-700': control.touched && control.invalid }"
    >
      {{ label }}
      @if (
        showRequiredIndicator &&
        (required || control.hasValidator(Validators.required))
      ) {
        <abbr title="required" class="text-red-700">*</abbr>
      }
    </label>
    <input
      #input
      appInputType
      [id]="id"
      [appInputType]="type"
      [name]="name"
      [value]="value"
      [placeholder]="placeholder"
      (focusout)="onTouch()"
      (input)="onChange(input.value)"
      class="block w-full rounded-lg border border-gray-300 bg-gray-50 p-2.5 text-gray-500 focus:border-primary-500 focus:outline-none focus:ring-primary-500 sm:text-sm"
      [ngClass]="{
        'border-red-500 bg-red-50 text-sm text-red-900 placeholder-red-700 focus:border-red-500 focus:ring-red-500':
          control.touched && control.invalid
      }"
      autocomplete="on"
    />
    @if (control.touched && control.invalid) {
      <p class="mt-2 text-sm text-red-600">
        <span class="font-medium">
          {{ getErrorMessage() }}
        </span>
      </p>
    }
  `,
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => InputComponent),
      multi: true,
    },
  ],
  imports: [
    NgClass,
    NgIf,
    InputTypeDirective,
    PasswordStrengthIndicatorComponent,
  ],
})
export class InputComponent extends BaseControlValueAccessorService<string> {
  protected readonly Validators = Validators;
  @Input({ required: true }) label: string;
  @Input({ required: true }) id: string;
  @Input({ required: true }) type: string;
  @Input({ required: true }) name: string;
  @Input() placeholder: string = '';
  @Input({ transform: booleanAttribute }) required = false;
  @Input({ transform: booleanAttribute }) showRequiredIndicator = false;
  @Input() dataPopoverTarget: string;
  @Input() dataPopoverPlacement: string;

  getErrorMessage(): string {
    if (this.control.errors?.['required']) return 'Ce champ est obligatoire';
    if (this.control.errors?.['email']) return 'Adresse email invalide';
    if (this.control.errors?.['minlength'])
      return `Ce champ doit contenir au moins ${this.control.errors['minlength'].requiredLength} caractères`;
    if (this.control.errors?.['maxlength'])
      return `Ce champ doit contenir au maximum ${this.control.errors['maxlength'].requiredLength} caractères`;
    if (this.control.errors?.['emailDomain'])
      return `L'adresse email doit appartenir au domaine ${this.control.errors['emailDomain'].requiredDomain}`;
    if (this.control.errors?.['pattern'])
      return `Ce champ doit respecter le format requis`;
    return '';
  }
}
