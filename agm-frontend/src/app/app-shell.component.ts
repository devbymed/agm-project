import { NgClass, NgIf } from '@angular/common';
import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { NavbarComponent } from './core/layout/navbar/navbar.component';

@Component({
  selector: 'app-main-layout',
  standalone: true,
  imports: [NavbarComponent, RouterOutlet, NgIf, NgClass],
  template: `
    <app-navbar />
    <div class="flex overflow-hidden bg-gray-50 pt-16">
      <div
        id="main-content"
        class="relative mx-auto h-full w-full max-w-screen-2xl overflow-y-auto bg-gray-50 dark:bg-gray-900"
      >
        <main>
          <router-outlet />
        </main>
      </div>
    </div>
  `,
})
export class AppShellComponent {}
