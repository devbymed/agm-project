import {
  Directive,
  ElementRef,
  Input,
  OnInit,
  Renderer2,
  inject,
} from '@angular/core';

@Directive({
  selector: '[appInputType]',
  standalone: true,
})
export class InputTypeDirective implements OnInit {
  private element = inject(ElementRef);
  private renderer = inject(Renderer2);

  @Input('inputType') type: string;

  ngOnInit(): void {
    const inputElement = this.element.nativeElement;

    if (this.type) {
      this.renderer.setAttribute(inputElement, 'type', this.type);
    } else {
      this.renderer.setAttribute(inputElement, 'type', 'text');
    }
  }
}
