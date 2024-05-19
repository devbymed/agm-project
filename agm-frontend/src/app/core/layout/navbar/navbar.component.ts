import { NgClass } from '@angular/common';
import {AfterViewInit, Component, inject, OnInit} from '@angular/core';
import {NavigationEnd, Router, RouterLink, RouterLinkActive} from '@angular/router';
import {StorageService} from "@core/services/storage.service";
import {AuthService} from "@core/auth/auth.service";
import {initFlowbite} from "flowbite";
import {filter} from "rxjs";

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [RouterLink, RouterLinkActive, NgClass],
  templateUrl: './navbar.component.html',
})
export class NavbarComponent implements AfterViewInit {
  private router = inject(Router);
  private authService = inject(AuthService);

  ngAfterViewInit(): void {
    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe(() => {
      initFlowbite();
    });
  }

  isRouteActive(route: string): boolean {
    return this.router.url.startsWith(route);
  }

  logout(): void {
    this.authService.logout();
  }
}
