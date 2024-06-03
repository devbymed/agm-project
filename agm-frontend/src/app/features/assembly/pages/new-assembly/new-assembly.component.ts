import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { ToastComponent } from "@shared/components/toast/taost.component";
import { ErrorTestComponent } from "../../../../error-test.component";

@Component({
  selector: 'app-new-assembly',
  standalone: true,
  imports: [RouterOutlet, ErrorTestComponent, ToastComponent],
  templateUrl: './new-assembly.component.html',
})
export class NewAssemblyComponent { }
