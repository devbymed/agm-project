import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router, RouterLink, RouterLinkActive, RouterOutlet } from "@angular/router";
import { NewAssemblyFormComponent } from "@features/assembly/components/new-assembly-form/new-assembly-form.component";
import { AssemblyStateService } from "@features/assembly/services/assembly-state.service";
import { initFlowbite } from "flowbite";
import { Subject, takeUntil } from "rxjs";

@Component({
  selector: 'app-new-assembly',
  standalone: true,
  imports: [NewAssemblyFormComponent, RouterOutlet, RouterLink, RouterLinkActive],
  templateUrl: './new-assembly.component.html',
})
export class NewAssemblyComponent implements OnInit, OnDestroy {
  hasCurrentAssembly: boolean = false;
  private destroy$ = new Subject<void>();

  constructor(private router: Router, private assemblyStateService: AssemblyStateService) { }

  ngOnInit(): void {
    this.assemblyStateService.hasCurrentAssembly$
      .pipe(takeUntil(this.destroy$))
      .subscribe((exists: boolean) => {
        this.hasCurrentAssembly = exists;
        if (exists) {
          this.router.navigate(['/preparation-assemblee/nouvelle-assemblee/assemblee-en-cours']);
        }
      });
    initFlowbite();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}