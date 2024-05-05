import { NgClass, NgIf } from '@angular/common';
import {
  Component,
  Injector,
  Input,
  OnInit,
  forwardRef,
  inject,
} from '@angular/core';
import {
  AbstractControl,
  ControlValueAccessor,
  FormControl,
  FormControlDirective,
  FormControlName,
  FormGroupDirective,
  NG_VALUE_ACCESSOR,
  NgControl,
  NgModel,
  ValidationErrors,
  ValidatorFn,
} from '@angular/forms';
import { initFlowbite } from 'flowbite';
import { InputTypeDirective } from '../directives/input-type.directive';

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
  template: `
    <label
      [for]="id"
      class="mb-2 block text-sm font-medium text-gray-500 dark:text-white"
      [ngClass]="{ 'text-red-700': control.touched && control.invalid }"
    >
      {{ label }}
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
    <p *ngIf="control.touched" class="mt-2 text-sm text-red-600">
      <span class="font-medium">
        {{ getErrorMessage() }}
      </span>
    </p>
  `,
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => InputComponent),
      multi: true,
    },
  ],
})
export class InputComponent implements ControlValueAccessor, OnInit {
  injector = inject(Injector);
  @Input({ required: true }) label: string;
  @Input({ required: true }) id: string;
  @Input({ required: true }) type: string;
  @Input({ required: true }) name: string;
  @Input() placeholder: string = '';

  value = '';
  control: FormControl;

  onTouch = () => {};

  onChange = (value: string) => {};

  ngOnInit(): void {
    initFlowbite();
    this.control = this.getControl();
  }

  writeValue(value: string): void {
    this.value = value;
  }

  registerOnChange(fn: any): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: any): void {
    this.onTouch = fn;
  }

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

  private getControl(): FormControl {
    const injectedControl = this.injector.get(NgControl);
    switch (injectedControl.constructor) {
      case NgModel:
        return (injectedControl as NgModel).control;
      case FormControlName:
        return this.injector
          .get(FormGroupDirective)
          .getControl(injectedControl as FormControlName);
      default:
        return (injectedControl as FormControlDirective).form as FormControl;
    }
  }
}
