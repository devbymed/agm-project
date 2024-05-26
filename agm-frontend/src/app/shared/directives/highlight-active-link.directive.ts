import { Directive, ElementRef, Input, Renderer2, inject } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
import { Subscription, filter } from 'rxjs';

@Directive({
  selector: '[appHighlightActiveLink]',
  standalone: true,
})
export class HighlightActiveLinkDirective {
  private subscription: Subscription;
  private element = inject(ElementRef);
  private renderer = inject(Renderer2);
  private router = inject(Router);

  @Input('appHighlightActiveLink') linkPath: string;
  @Input() activeClass = 'text-primary-700';

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
