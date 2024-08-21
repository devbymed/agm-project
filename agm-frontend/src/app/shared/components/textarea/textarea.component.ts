import { NgClass } from '@angular/common';
import { Component, Input, booleanAttribute, forwardRef } from '@angular/core';
import { NG_VALUE_ACCESSOR, Validators } from '@angular/forms';
import { BaseControlValueAccessorService } from '@core/services/base-control-value-accessor.service';
import { ValidationErrorService } from '@core/services/validation-error.service';

@Component({
  selector: 'app-textarea',
  standalone: true,
  imports: [NgClass],
  templateUrl: './textarea.component.html',
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => TextareaComponent),
      multi: true,
    },
  ],
})
export class TextareaComponent extends BaseControlValueAccessorService<string> {
  protected readonly Validators = Validators;

  @Input({ required: true }) label: string;
  @Input({ required: true }) id: string;
  @Input() placeholder: string = '';
  @Input() disabled = false;
  @Input({ transform: booleanAttribute }) required = false;
  @Input({ transform: booleanAttribute }) showRequiredIndicator = false;

  constructor(public validationErrorService: ValidationErrorService) {
    super();
  }
}
