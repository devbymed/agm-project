import { NgClass, NgFor } from '@angular/common';
import { Component, Input, booleanAttribute, forwardRef } from '@angular/core';
import { FormsModule, NG_VALUE_ACCESSOR, Validators } from '@angular/forms';
import { BaseControlValueAccessorService } from '@core/services/base-control-value-accessor.service';
import { ValidationErrorService } from '@core/services/validation-error.service';

interface SelectOption {
  value: string;
  label: string;
}

@Component({
  selector: 'app-select',
  standalone: true,
  imports: [NgClass, NgFor, FormsModule],
  templateUrl: './select.component.html',
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => SelectComponent),
      multi: true,
    },
  ],
})
export class SelectComponent extends BaseControlValueAccessorService<string> {
  protected readonly Validators = Validators;

  @Input({ required: false }) label: string;
  @Input({ required: true }) id: string;
  @Input({ required: true }) name: string;
  @Input() options: SelectOption[] = [];
  @Input() placeholder: string = '';
  @Input() selected: string = '';
  @Input({ transform: booleanAttribute }) required = false;
  @Input({ transform: booleanAttribute }) showRequiredIndicator = false;

  constructor(public validationErrorService: ValidationErrorService) {
    super();
  }
}
