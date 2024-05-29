import { AsyncPipe } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { AuthService } from '@core/auth/auth.service';
import { User } from "@core/models/user.model";
import { HighlightActiveLinkDirective } from '@shared/directives/highlight-active-link.directive';
import { initFlowbite } from 'flowbite';
import { Observable } from "rxjs";

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [
    AsyncPipe,
    RouterLink,
    RouterLinkActive,
    HighlightActiveLinkDirective,
  ],
  templateUrl: './header.component.html',
})
export class HeaderComponent implements OnInit {
  private authService = inject(AuthService);
  private router = inject(Router);
  user$: Observable<User | null>;

  ngOnInit(): void {
    this.user$ = this.authService.getUser();
    initFlowbite();
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/auth/connexion']);
  }
}
