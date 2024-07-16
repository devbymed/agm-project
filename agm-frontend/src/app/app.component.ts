import { registerLocaleData } from "@angular/common";
import localeFr from "@angular/common/locales/fr"; // Import the necessary locale data
import { Component } from "@angular/core";
import { RouterOutlet } from "@angular/router";

@Component({
  selector: "app-root",
  standalone: true,
  imports: [RouterOutlet],
  templateUrl: "./app.component.html"
})
export class AppComponent {

  constructor() {
    registerLocaleData(localeFr, 'fr');
  }
}

