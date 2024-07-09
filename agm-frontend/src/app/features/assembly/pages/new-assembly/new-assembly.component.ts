import { Component, OnInit } from '@angular/core';
import { Router, RouterLink, RouterLinkActive, RouterOutlet } from "@angular/router";
import { NewAssemblyFormComponent } from "@features/assembly/components/new-assembly-form/new-assembly-form.component";
import { AssemblyService } from "@features/assembly/services/assembly.service";

@Component({
  selector: 'app-new-assembly',
  standalone: true,
  imports: [NewAssemblyFormComponent, RouterOutlet, RouterLink, RouterLinkActive],
  templateUrl: './new-assembly.component.html',
})
export class NewAssemblyComponent implements OnInit {
  hasCurrentAssembly: boolean = false;

  constructor(private router: Router, private assemblyService: AssemblyService) { }

  ngOnInit(): void {
    this.checkCurrentAssembly();
    this.router.navigate(['/preparation-assemblee/nouvelle-assemblee/assemblee-en-cours']);
  }

  checkCurrentAssembly(): void {
    this.assemblyService.hasCurrentAssembly().subscribe((exists: boolean) => {
      this.hasCurrentAssembly = exists;
    });
  }
}