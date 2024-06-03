import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { ToastComponent } from "@shared/components/toast/taost.component";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [ToastComponent, RouterOutlet],
  templateUrl: './app.component.html',
})
export class AppComponent { }
