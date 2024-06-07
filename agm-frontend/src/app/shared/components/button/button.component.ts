import { NgClass } from "@angular/common";
import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-button',
  standalone: true,
  imports: [NgClass],
  templateUrl: './button.component.html',
})
export class ButtonComponent {
  @Input() type: 'button' | 'submit' = 'button';
  @Input() buttonType: string = 'default';
  buttonClass: string = '';

  ngOnInit() {
    this.setButtonClass();
  }

  setButtonClass() {
    const buttonClasses: { [key: string]: string } = {
      'default': 'text-white bg-primary-700 hover:bg-primary-800 focus:ring-primary-100',
      'light': 'text-primary-700 bg-white border border-gray-300 hover:bg-gray-100 focus:ring-gray-100',
    };

    this.buttonClass = buttonClasses[this.buttonType] || buttonClasses['default'];
  }
}
