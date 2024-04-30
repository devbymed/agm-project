import { Component, OnInit } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { initFlowbite } from 'flowbite';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet],
  template: `
    <main class="bg-gray-50 dark:bg-gray-900"><router-outlet /></main>
  `,
  styles: [],
})
export class AppComponent implements OnInit {
  ngOnInit(): void {
    initFlowbite();
  }
}
