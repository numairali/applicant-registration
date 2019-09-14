import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { IApplicant, Applicant } from 'app/shared/model/applicant.model';
import { ApplicantService } from './applicant.service';

@Component({
  selector: 'jhi-applicant-update',
  templateUrl: './applicant-update.component.html'
})
export class ApplicantUpdateComponent implements OnInit {
  applicant: IApplicant;
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    first_name: [null, [Validators.required, Validators.minLength(2), Validators.maxLength(50)]],
    last_name: [null, [Validators.required, Validators.minLength(2), Validators.maxLength(50)]],
    email: [null, [Validators.required]],
    phone_number: [null, [Validators.required, Validators.minLength(10), Validators.maxLength(10)]]
  });

  constructor(protected applicantService: ApplicantService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ applicant }) => {
      this.updateForm(applicant);
      this.applicant = applicant;
    });
  }

  updateForm(applicant: IApplicant) {
    this.editForm.patchValue({
      id: applicant.id,
      first_name: applicant.first_name,
      last_name: applicant.last_name,
      email: applicant.email,
      phone_number: applicant.phone_number
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const applicant = this.createFromForm();
    if (applicant.id !== undefined) {
      this.subscribeToSaveResponse(this.applicantService.update(applicant));
    } else {
      this.subscribeToSaveResponse(this.applicantService.create(applicant));
    }
  }

  private createFromForm(): IApplicant {
    const entity = {
      ...new Applicant(),
      id: this.editForm.get(['id']).value,
      first_name: this.editForm.get(['first_name']).value,
      last_name: this.editForm.get(['last_name']).value,
      email: this.editForm.get(['email']).value,
      phone_number: this.editForm.get(['phone_number']).value
    };
    return entity;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IApplicant>>) {
    result.subscribe((res: HttpResponse<IApplicant>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
}
