import { Directive, ElementRef, Input, Renderer2 } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
import { Subscription, filter } from 'rxjs';

@Directive({
  selector: '[appHighlightActiveLink]',
  standalone: true,
})
export class HighlightActiveLinkDirective {
  @Input('appHighlightActiveLink') linkPath: string;
  @Input() activeClass = 'text-primary-700';
  private subscription: Subscription;

  constructor(
    private router: Router,
    private element: ElementRef,
    private renderer: Renderer2,
  ) {}

  ngOnInit(): void {
    this.subscription = this.router.events
      .pipe(filter((event) => event instanceof NavigationEnd))
      .subscribe(() => this.updateActiveClass());
    this.updateActiveClass();
  }

  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

  private updateActiveClass(): void {
    const currentUrl = this.router.url;
    if (currentUrl.includes(this.linkPath)) {
      this.renderer.addClass(this.element.nativeElement, this.activeClass);
    } else {
      this.renderer.removeClass(this.element.nativeElement, this.activeClass);
    }
  }
}
