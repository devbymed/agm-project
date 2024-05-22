import { NgClass, NgIf } from '@angular/common';
import {
  Component,
  Input,
  OnChanges,
  OnDestroy,
  SimpleChanges,
} from '@angular/core';
import { FormControl } from '@angular/forms';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-password-strength-indicator',
  standalone: true,
  imports: [NgIf, NgClass],
  template: `
    <div
      data-popover
      id="popover-password"
      role="tooltip"
      class="invisible absolute z-10 inline-block w-72 rounded-lg border border-gray-200 bg-white text-sm font-light text-gray-500 opacity-0 shadow-sm transition-opacity duration-300"
    >
      <div class="space-y-2 p-3">
        <h3 class="font-semibold text-gray-900 dark:text-white">
          Doit contenir au moins 8 caractères
        </h3>
        <div class="grid grid-cols-4 gap-2">
          <div
            class="h-1"
            [ngClass]="{
              'bg-gray-200':
                !(hasUpperCase && hasLowerCase) && !passwordControl.value,
              'bg-primary-400': hasUpperCase && hasLowerCase
            }"
          ></div>
          <div
            class="h-1"
            [ngClass]="{
              'bg-gray-200': !hasSymbol && !passwordControl.value,
              'bg-primary-400': hasSymbol && passwordControl.value
            }"
          ></div>
          <div
            class="h-1"
            [ngClass]="{
              'bg-gray-200 dark:bg-gray-600':
                !hasNumber && !passwordControl.value,
              'bg-primary-400': hasNumber
            }"
          ></div>
          <div
            class="h-1"
            [ngClass]="{
              'bg-gray-200 dark:bg-gray-600':
                !isValidLength && !passwordControl.value,
              'bg-primary-400': isValidLength
            }"
          ></div>
        </div>
        <p>Il est préférable d'avoir :</p>
        <ul>
          <li
            class="mb-1 flex items-center"
            [ngClass]="{
              'text-primary-400': hasUpperCase && hasLowerCase,
              'text-gray-300': !hasUpperCase || !hasLowerCase
            }"
          >
            <svg
              class="mr-2 h-4 w-4"
              aria-hidden="true"
              fill="currentColor"
              viewBox="0 0 20 20"
              xmlns="http://www.w3.org/2000/svg"
            >
              <path
                *ngIf="hasUpperCase && hasLowerCase"
                fill-rule="evenodd"
                d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z"
                clip-rule="evenodd"
              ></path>
              <path
                *ngIf="!hasUpperCase || !hasLowerCase"
                fill-rule="evenodd"
                d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z"
                clip-rule="evenodd"
              ></path>
            </svg>
            Lettres majuscules et minuscules
          </li>
          <li
            class="mb-1 flex items-center"
            [ngClass]="{
              'text-primary-400': hasSymbol,
              'text-gray-300': !hasSymbol
            }"
          >
            <svg
              class="mr-2 h-4 w-4"
              aria-hidden="true"
              fill="currentColor"
              viewBox="0 0 20 20"
              xmlns="http://www.w3.org/2000/svg"
            >
              <path
                *ngIf="hasSymbol"
                fill-rule="evenodd"
                d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z"
                clip-rule="evenodd"
              ></path>
              <path
                *ngIf="!hasSymbol"
                fill-rule="evenodd"
                d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z"
                clip-rule="evenodd"
              ></path>
            </svg>
            Un symbole (#$&)
          </li>
          <li
            class="mb-1 flex items-center"
            [ngClass]="{
              'text-primary-400': hasNumber,
              'text-gray-300': !hasNumber
            }"
          >
            <svg
              class="mr-2 h-4 w-4"
              aria-hidden="true"
              fill="currentColor"
              viewBox="0 0 20 20"
              xmlns="http://www.w3.org/2000/svg"
            >
              <path
                *ngIf="hasNumber"
                fill-rule="evenodd"
                d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z"
                clip-rule="evenodd"
              ></path>
              <path
                *ngIf="!hasNumber"
                fill-rule="evenodd"
                d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z"
                clip-rule="evenodd"
              ></path>
            </svg>
            Un chiffre (0-9)
          </li>
          <!-- <li
            class="flex items-center"
            [ngClass]="{
              'text-primary-400': isValidLength,
              'text-gray-300': !isValidLength
            }"
          >
            <svg
              class="mr-2 h-4 w-4"
              aria-hidden="true"
              fill="currentColor"
              viewBox="0 0 20 20"
              xmlns="http://www.w3.org/2000/svg"
            >
              <path
                *ngIf="isValidLength"
                fill-rule="evenodd"
                d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z"
                clip-rule="evenodd"
              ></path>
              <path
                *ngIf="!isValidLength"
                fill-rule="evenodd"
                d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z"
                clip-rule="evenodd"
              ></path>
            </svg>
            Au moins 8 caractères
          </li> -->
        </ul>
      </div>
      <div data-popper-arrow></div>
    </div>
  `,
  styles: ``,
})
export class PasswordStrengthIndicatorComponent
  implements OnChanges, OnDestroy
{
  @Input({ required: true }) passwordControl: FormControl;

  hasUpperCase = false;
  hasLowerCase = false;
  hasNumber = false;
  hasSymbol = false;
  isValidLength = false;
  private subscription: Subscription;

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['passwordControl']) {
      if (this.subscription) {
        this.subscription.unsubscribe();
      }
      this.subscription = this.passwordControl.valueChanges.subscribe(
        (value) => {
          this.checkPassword(value);
        },
      );
    }
  }

  private checkPassword(password: string): void {
    const upperCasePattern = /[A-Z]/;
    const lowerCasePattern = /[a-z]/;
    const numberPattern = /[0-9]/;
    const symbolPattern = /[!@#$%&*()_+\-=\[\]{};':"\\|,.<>\/?]/;

    this.hasUpperCase = upperCasePattern.test(password);
    this.hasLowerCase = lowerCasePattern.test(password);
    this.hasNumber = numberPattern.test(password);
    this.hasSymbol = symbolPattern.test(password);
    this.isValidLength = password.length >= 8;
  }

  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }
}
