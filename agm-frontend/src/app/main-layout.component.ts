import { NgClass, NgIf } from '@angular/common';
import { Component } from '@angular/core';
import { ActivatedRoute, Router, RouterOutlet } from '@angular/router';
import { NavbarComponent } from './shared/components/navbar/navbar.component';
import { SidebarComponent } from './shared/components/sidebar/sidebar.component';

@Component({
  selector: 'app-main-layout',
  standalone: true,
  imports: [NavbarComponent, SidebarComponent, RouterOutlet, NgIf, NgClass],
  template: `
    <app-navbar />
    <div class="flex overflow-hidden bg-gray-50 pt-16">
      @if (showSidebar) {
        <app-sidebar />
      }
      <div
        id="main-content"
        class="relative mx-auto h-full w-full max-w-screen-2xl overflow-y-auto bg-gray-50"
        [ngClass]="{
          'relative h-full w-full overflow-y-auto bg-gray-50 lg:ml-64':
            showSidebar
        }"
      >
        <main>
          <router-outlet />
        </main>
      </div>
    </div>
  `,
})
export class MainLayoutComponent {
  showSidebar: boolean;

  constructor(
    private router: Router,
    private route: ActivatedRoute,
  ) {
    this.router.events.subscribe(() => {
      // List of routes where you want to show the sidebar
      const sidebarRoutes = ['nouvelle-assemblee'];

      // Check if the current route is in the list
      this.showSidebar = sidebarRoutes.includes(
        this.route.snapshot.firstChild?.url[0]?.path ?? '',
      );
    });
  }
}
