import { NgClass } from "@angular/common";
import { Component, Input, OnInit, booleanAttribute, forwardRef } from '@angular/core';
import { NG_VALUE_ACCESSOR, Validators } from "@angular/forms";
import { BaseControlValueAccessorService } from "@core/services/base-control-value-accessor.service";
import { ValidationErrorService } from "@core/services/validation-error.service";

@Component({
  selector: 'app-select',
  standalone: true,
  imports: [NgClass],
  templateUrl: './select.component.html',
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => SelectComponent),
      multi: true,
    },
  ],
})
export class SelectComponent extends BaseControlValueAccessorService<string> implements OnInit {
  protected readonly Validators = Validators;

  @Input({ required: true }) label: string;
  @Input({ required: true }) id: string;
  @Input({ required: true }) name: string;
  @Input() options: { value: string | number, label: string }[] = [];
  @Input() placeholder: string = '';
  @Input({ transform: booleanAttribute }) required = false;
  @Input({ transform: booleanAttribute }) showRequiredIndicator = false;

  constructor(public validationErrorService: ValidationErrorService) {
    super();
  }
}
