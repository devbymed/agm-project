import { Component } from '@angular/core';

@Component({
  selector: 'app-error-test',
  standalone: true,
  imports: [],
  template: `
    <button (click)="triggerError()">Trigger Error</button>
  `,
})
export class ErrorTestComponent {
  triggerError(): void {
    throw new Error('Test Error');
  }
}
