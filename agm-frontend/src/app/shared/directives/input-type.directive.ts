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

  @Input('appInputType') type: string;

  ngOnInit(): void {
    const inputElement = this.element.nativeElement;
    this.renderer.setAttribute(inputElement, 'type', this.type);
  }
}
