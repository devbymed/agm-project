import { NgIf, NgStyle } from "@angular/common";
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms";
import { InputComponent } from "@shared/components/form/input/input.component";
import { initFlowbite } from "flowbite";
import { ToastrService } from "ngx-toastr";

@Component({
  selector: 'app-fdr-follow-up',
  standalone: true,
  imports: [NgIf, NgStyle, InputComponent, ReactiveFormsModule],
  templateUrl: './fdr-follow-up.component.html',
})
export class FdrFollowUpComponent implements OnInit {
  updateActionForm: FormGroup;

  constructor(private fb: FormBuilder, private toastr: ToastrService
  ) { }

  ngOnInit() {
    initFlowbite();
    this.updateActionForm = this.fb.group({
      description: ['', Validators.required],
      entity: ['', Validators.required],
      responsible: ['', Validators.required],
      startDate: ['', Validators.required],
      endDate: ['', Validators.required],
      deliverable: ['', Validators.required],
      progressStatus: ['', Validators.required],
      realizationDate: ['', Validators.required],
      observation: ['', Validators.required],
    });
  }
}
