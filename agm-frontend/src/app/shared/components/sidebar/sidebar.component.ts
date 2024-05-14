import { NgFor } from '@angular/common';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { Subscription } from 'rxjs';
import { SidebarService } from '../../../core/services/sidebar.service';

interface SidebarLink {
  label: string;
  path: string;
}

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [RouterLink, RouterLinkActive, NgFor],
  templateUrl: './sidebar.component.html',
})
export class SidebarComponent implements OnInit, OnDestroy {
  links: SidebarLink[] = [];
  private linksSubscription: Subscription;

  constructor(private sidebarService: SidebarService) {}

  ngOnInit() {
    this.linksSubscription = this.sidebarService.links$.subscribe((links) => {
      this.links = links;
    });
  }

  ngOnDestroy() {
    if (this.linksSubscription) {
      this.linksSubscription.unsubscribe();
    }
  }
}
