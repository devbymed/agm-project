import { Injectable } from '@angular/core';
import { Toast } from "@core/models/toast.model";
import { Subject } from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class ToastService {
  private toastSubject = new Subject<Toast>();
  toastMessages = this.toastSubject.asObservable();

  showToast(toast: Toast) {
    this.toastSubject.next(toast);
  }
}
