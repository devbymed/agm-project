import { Component, OnInit } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { initFlowbite } from 'flowbite';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet],
  template: ` <main class="bg-gray-50"><router-outlet /></main> `,
})
export class AppComponent implements OnInit {
  ngOnInit(): void {
    initFlowbite();
  }
}
