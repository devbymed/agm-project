import {Component, inject, OnInit} from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { NavbarComponent } from '@core/layout/navbar/navbar.component';
import { initFlowbite } from 'flowbite';
import {AuthService} from "@core/auth/auth.service";
import {map, Observable} from "rxjs";
import {AsyncPipe, NgIf} from "@angular/common";
import LoginPage from "@core/auth/pages/login/login.page";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, AsyncPipe, NgIf, NavbarComponent, LoginPage],
  templateUrl: './app.component.html',
})
export class AppComponent implements OnInit {
  private authService = inject(AuthService);
  isAuthenticated$: Observable<boolean>;

  ngOnInit(): void {
    this.isAuthenticated$ = this.authService.loginResponse$.pipe(
      map(user => !!user)
    );
    initFlowbite();
  }
}
