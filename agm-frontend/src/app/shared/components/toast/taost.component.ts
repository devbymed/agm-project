import { NgClass } from "@angular/common";
import { Component, OnDestroy, OnInit } from '@angular/core';
import { Toast } from "@core/models/toast.model";
import { ToastService } from "@core/services/toast.service";
import { Subscription } from "rxjs";

@Component({
  selector: 'app-toast',
  standalone: true,
  imports: [NgClass],
  templateUrl: './toast.component.html',
})
export class ToastComponent implements OnInit, OnDestroy {
  toasts: Toast[] = [];
  subscription: Subscription;

  constructor(private toastService: ToastService) { }

  ngOnInit() {
    this.subscription = this.toastService.toastMessages.subscribe((toast) => {
      this.toasts.push(toast);
      setTimeout(() => this.removeToast(toast), 3000);
    });
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }

  removeToast(toast: Toast) {
    this.toasts = this.toasts.filter(t => t !== toast);
  }

  getToastClass(type: string) {
    switch (type) {
      case 'success': return 'bg-primary-100 text-primary-500';
      case 'error': return 'bg-red-100 text-red-500';
      case 'warning': return 'bg-warning-100 text-warning-500';
      default: return 'bg-white text-gray-500';
    }
  }

  getIconClass(type: string) {
    switch (type) {
      case 'success': return 'text-primary-500 bg-primary-100';
      case 'error': return 'text-red-500 bg-red-100';
      case 'warning': return 'text-warning-500 bg-warning-100';
      default: return '';
    }
  }

  getIconPath(type: string) {
    switch (type) {
      case 'success': return 'M10 .5a9.5 9.5 0 1 0 9.5 9.5A9.51 9.51 0 0 0 10 .5Zm3.707 8.207-4 4a1 1 0 0 1-1.414 0l-2-2a1 1 0 0 1 1.414-1.414L9 10.586l3.293-3.293a1 1 0 0 1 1.414 1.414Z';
      case 'error': return 'M10 .5a9.5 9.5 0 1 0 9.5 9.5A9.51 9.51 0 0 0 10 .5Zm3.707 11.793a1 1 0 1 1-1.414 1.414L10 11.414l-2.293 2.293a1 1 0 0 1-1.414-1.414L8.586 10 6.293 7.707a1 1 0 0 1 1.414-1.414L10 8.586l2.293-2.293a1 1 0 0 1 1.414 1.414L11.414 10l2.293 2.293Z';
      case 'warning': return 'M10 .5a9.5 9.5 0 1 0 9.5 9.5A9.51 9.51 0 0 0 10 .5ZM10 15a1 1 0 1 1 0-2 1 1 0 0 1 0 2Zm1-4a1 1 0 0 1-2 0V6a1 1 0 0 1 2 0v5Z';
      default: return '';
    }
  }
} 
