import { inject } from "@angular/core";
import { CanActivateFn, Router } from '@angular/router';
import { map } from "rxjs";
import { AssemblyService } from "../services/assembly.service";

export const currentAssemblyGuard: CanActivateFn = (route, state) => {
  const assemblyService = inject(AssemblyService);
  const router = inject(Router);

  return assemblyService.hasCurrentAssembly().pipe(
    map((exists: boolean) => {
      if (exists) {
        return true;
      } else {
        router.navigate(['/preparation-assemblee/nouvelle-assemblee']);
        return false;
      }
    })
  );
};
