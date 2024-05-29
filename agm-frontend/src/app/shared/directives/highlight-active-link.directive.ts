import { Directive, ElementRef, Input, OnDestroy, OnInit, Renderer2, inject } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
import { Subject, filter, takeUntil } from 'rxjs';

@Directive({
  selector: '[appHighlightActiveLink]',
  standalone: true,
})
export class HighlightActiveLinkDirective implements OnInit, OnDestroy {
  private element = inject(ElementRef);
  private renderer = inject(Renderer2);
  private router = inject(Router);
  private destroy$ = new Subject<void>();

  @Input('appHighlightActiveLink') linkPath: string;
  @Input() activeClass = 'text-primary-700';

  ngOnInit(): void {
    this.router.events
      .pipe(
        filter((event) => event instanceof NavigationEnd),
        takeUntil(this.destroy$)
      )
      .subscribe(() => this.updateActiveClass());
    this.updateActiveClass();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  private updateActiveClass(): void {
    const currentUrl = this.router.url;
    if (this.linkPath && currentUrl.includes(this.linkPath)) {
      this.renderer.addClass(this.element.nativeElement, this.activeClass);
    } else {
      this.renderer.removeClass(this.element.nativeElement, this.activeClass);
    }
  }
}
