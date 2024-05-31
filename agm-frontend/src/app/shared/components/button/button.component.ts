import { NgClass } from "@angular/common";
import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-button',
  standalone: true,
  imports: [NgClass],
  templateUrl: './button.component.html',
})
export class ButtonComponent {
  @Input({ required: true }) type: 'submit' | 'button' | 'reset' = 'button';
  @Input() buttonType: string = 'default';
  buttonClass: string = '';

  ngOnInit() {
    this.setButtonClass();
  }

  setButtonClass() {
    const buttonClasses: { [key: string]: string } = {
      'default': 'text-white bg-primary-700 hover:bg-primary-800 focus:ring-primary-300',
      'light': 'text-gray-900 bg-white border border-gray-300 focus:outline-none hover:bg-gray-100 focus:ring-gray-100',
      'warning': 'text-white bg-warning-600 hover:bg-warning-700 focus:ring-warning-300',
      'danger': 'text-white bg-red-600 hover:bg-red-700 focus:ring-red-300',
    };

    this.buttonClass = buttonClasses[this.buttonType] || buttonClasses['default'];
  }
}
