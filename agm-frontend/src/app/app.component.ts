import { AfterViewInit, Component, inject } from '@angular/core';
import { NavigationEnd, Router, RouterOutlet } from '@angular/router';
import { initFlowbite } from 'flowbite';
import { filter } from 'rxjs/operators';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet],
  template: `<router-outlet />`,
})
export class AppComponent implements AfterViewInit {
  private router = inject(Router);

  ngAfterViewInit(): void {
    this.initializeFlowbite();
    this.router.events
      .pipe(filter((event) => event instanceof NavigationEnd))
      .subscribe(() => {
        this.initializeFlowbite();
      });
  }

  private initializeFlowbite(): void {
    initFlowbite();
  }
}
