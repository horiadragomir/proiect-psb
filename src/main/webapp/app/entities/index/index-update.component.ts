import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IIndex, Index } from 'app/shared/model/index.model';
import { IndexService } from './index.service';
import { ILocation } from 'app/shared/model/location.model';
import { LocationService } from 'app/entities/location/location.service';

@Component({
  selector: 'jhi-index-update',
  templateUrl: './index-update.component.html',
})
export class IndexUpdateComponent implements OnInit {
  isSaving = false;
  locations: ILocation[] = [];

  editForm = this.fb.group({
    id: [],
    value: [null, [Validators.required]],
    month: [null, [Validators.required]],
    year: [null, [Validators.required]],
    locationId: [],
  });

  constructor(
    protected indexService: IndexService,
    protected locationService: LocationService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ index }) => {
      this.updateForm(index);

      this.locationService.query().subscribe((res: HttpResponse<ILocation[]>) => (this.locations = res.body || []));
    });
  }

  updateForm(index: IIndex): void {
    this.editForm.patchValue({
      id: index.id,
      value: index.value,
      month: index.month,
      year: index.year,
      locationId: index.locationId,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const index = this.createFromForm();
    if (index.id !== undefined) {
      this.subscribeToSaveResponse(this.indexService.update(index));
    } else {
      this.subscribeToSaveResponse(this.indexService.create(index));
    }
  }

  private createFromForm(): IIndex {
    return {
      ...new Index(),
      id: this.editForm.get(['id'])!.value,
      value: this.editForm.get(['value'])!.value,
      month: this.editForm.get(['month'])!.value,
      year: this.editForm.get(['year'])!.value,
      locationId: this.editForm.get(['locationId'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IIndex>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: ILocation): any {
    return item.id;
  }
}
