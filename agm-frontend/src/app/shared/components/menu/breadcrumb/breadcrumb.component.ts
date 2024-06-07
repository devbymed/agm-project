import { AsyncPipe, NgClass, NgFor, NgIf } from "@angular/common";
import { Component, OnInit, inject } from '@angular/core';
import { RouterLink } from "@angular/router";
import { BreadcrumbService } from "@core/services/breadcrumb.service";
import { Observable } from "rxjs";

@Component({
  selector: 'app-breadcrumb',
  standalone: true,
  imports: [NgFor, NgIf, NgClass, AsyncPipe, RouterLink],
  templateUrl: './breadcrumb.component.html',
})
export class BreadcrumbComponent implements OnInit {
  private breadcrumbService = inject(BreadcrumbService);
  breadcrumbs$: Observable<Array<{ label: string, url: string }>>;

  ngOnInit(): void {
    this.breadcrumbs$ = this.breadcrumbService.breadcrumbs$;
  }
}
