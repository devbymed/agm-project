import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { HeaderComponent } from '@core/layout/navbar/header.component';
import { BreadcrumbComponent } from "@shared/components/menu/breadcrumb/breadcrumb.component";
import { FooterComponent } from "../footer/footer.component";

@Component({
  selector: 'main-layout',
  standalone: true,
  imports: [RouterOutlet, BreadcrumbComponent, HeaderComponent, FooterComponent],
  templateUrl: './main-layout.component.html',
})
export class MainLayoutComponent {
}
