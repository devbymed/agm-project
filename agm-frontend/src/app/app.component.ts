import { Component, OnInit } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { NavbarComponent } from '@core/layout/navbar/navbar.component';
import { initFlowbite } from 'flowbite';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, NavbarComponent],
  template: ` @if (isAuthenticated) {
      <app-navbar />
      <div class="flex overflow-hidden bg-gray-50 pt-16">
        <div
          id="main-content"
          class="relative mx-auto h-full w-full max-w-screen-2xl overflow-y-auto bg-gray-50"
        >
          <main>
            <router-outlet />
          </main>
        </div>
      </div>
    } @else {
      <router-outlet />
    }`,
})
export class AppComponent implements OnInit {
  isAuthenticated = true;

  ngOnInit(): void {
    initFlowbite();
  }
}
