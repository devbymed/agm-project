import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

export interface SidebarLink {
  label: string;
  path: string;
}

@Injectable({
  providedIn: 'root',
})
export class SidebarService {
  private linksSource = new BehaviorSubject<SidebarLink[]>([]);
  links$ = this.linksSource.asObservable();

  setLinks(links: SidebarLink[]) {
    this.linksSource.next(links);
  }
}
