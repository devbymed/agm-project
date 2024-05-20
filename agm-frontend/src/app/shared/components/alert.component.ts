import { NgClass } from '@angular/common';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-alert',
  standalone: true,
  imports: [NgClass],
  template: `
    <div class="rounded-lg p-4 text-sm" [ngClass]="alertClass" role="alert">
      <span class="font-medium">{{ message }}</span>
    </div>
  `,
})
export class AlertComponent {
  @Input({ required: true }) message!: string;
  @Input({ required: true }) type: 'success' | 'danger' | 'warning' | 'info';

  get alertClass() {
    switch (this.type) {
      case 'success':
        return 'bg-green-50 text-green-800';
      case 'danger':
        return 'bg-red-50 text-red-800';
      case 'warning':
        return 'bg-yellow-50 text-yellow-800';
      case 'info':
      default:
        return 'bg-blue-50 text-blue-800';
    }
  }
}
