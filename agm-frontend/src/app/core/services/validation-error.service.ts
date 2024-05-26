import { Injectable } from '@angular/core';
import { ValidationErrors } from '@angular/forms';

@Injectable({
  providedIn: 'root',
})
export class ValidationErrorService {
  getErrorMessage(errors: ValidationErrors | null): string {
    if (!errors) {
      return '';
    }
    if (errors['required']) {
      return 'Ce champ est obligatoire';
    }
    if (errors['email']) {
      return 'Adresse email invalide';
    }
    if (errors['minlength']) {
      return `Ce champ doit contenir au moins ${errors['minlength'].requiredLength} caractères`;
    }
    if (errors['maxlength']) {
      return `Ce champ doit contenir au maximum ${errors['maxlength'].requiredLength} caractères`;
    }
    if (errors['emailDomain']) {
      return `L'adresse email doit appartenir au domaine ${errors['emailDomain'].requiredDomain}`;
    }
    if (errors['pattern']) {
      return `Ce champ doit respecter le format requis`;
    }
    return '';
  }
}
