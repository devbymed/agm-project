import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from "rxjs";
import { AssemblyService } from "./assembly.service";

@Injectable({
  providedIn: 'root'
})
export class AssemblyStateService {

  private hasCurrentAssemblySubject = new BehaviorSubject<boolean>(false);
  private currentAssemblyDetailsSubject = new BehaviorSubject<any>(null);

  hasCurrentAssembly$: Observable<boolean> = this.hasCurrentAssemblySubject.asObservable();
  currentAssemblyDetails$: Observable<any> = this.currentAssemblyDetailsSubject.asObservable();

  constructor(private assemblyService: AssemblyService) {
    this.checkCurrentAssembly();
    this.fetchCurrentAssemblyDetails();
  }

  checkCurrentAssembly(): void {
    this.assemblyService.hasCurrentAssembly().subscribe({
      next: (exists: boolean) => {
        this.hasCurrentAssemblySubject.next(exists);
        if (exists) {
          this.fetchCurrentAssemblyDetails();
        } else {
          this.currentAssemblyDetailsSubject.next(null);
        }
      },
      error: (error) => {
        console.error('Error checking for current assembly:', error);
        this.hasCurrentAssemblySubject.next(false);
        this.currentAssemblyDetailsSubject.next(null);
      }
    });
  }

  fetchCurrentAssemblyDetails(): void {
    this.assemblyService.getCurrentAssemblyDetails().subscribe({
      next: (details: any) => {
        this.currentAssemblyDetailsSubject.next(details.data);
      },
      error: (error) => {
        console.error('Error fetching current assembly details:', error);
        this.currentAssemblyDetailsSubject.next(null);
      }
    });
  }

  updateCurrentAssemblyDetails(details: any): void {
    this.currentAssemblyDetailsSubject.next(details);
  }

  updateCurrentAssemblyState(state: boolean): void {
    this.hasCurrentAssemblySubject.next(state);
    if (!state) {
      this.currentAssemblyDetailsSubject.next(null);
    }
  }
}
