import { AsyncPipe } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { AuthService } from '@core/auth/auth.service';
import { User } from '@core/models/user.model';
import { HighlightActiveLinkDirective } from '@shared/directives/highlight-active-link.directive';
import { initFlowbite } from 'flowbite';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [
    AsyncPipe,
    RouterLink,
    RouterLinkActive,
    HighlightActiveLinkDirective,
  ],
  templateUrl: './navbar.component.html',
})
export class NavbarComponent implements OnInit {
  private authService = inject(AuthService);
  private router = inject(Router);

  user: User | null = this.authService.authResponse?.user ?? null;

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/auth']);
  }

  ngOnInit(): void {
    initFlowbite();
  }
}
