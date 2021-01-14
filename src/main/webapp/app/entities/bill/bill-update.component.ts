import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IBill, Bill } from 'app/shared/model/bill.model';
import { BillService } from './bill.service';
import { ILocation } from 'app/shared/model/location.model';
import { LocationService } from 'app/entities/location/location.service';

@Component({
  selector: 'jhi-bill-update',
  templateUrl: './bill-update.component.html',
})
export class BillUpdateComponent implements OnInit {
  isSaving = false;
  locations: ILocation[] = [];

  editForm = this.fb.group({
    id: [],
    firstDay: [null, [Validators.required]],
    lastDay: [null, [Validators.required]],
    value: [null, [Validators.required]],
    paid: [],
    locationId: [],
  });

  constructor(
    protected billService: BillService,
    protected locationService: LocationService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ bill }) => {
      if (!bill.id) {
        const today = moment().startOf('day');
        bill.firstDay = today;
        bill.lastDay = today;
      }

      this.updateForm(bill);

      this.locationService.query().subscribe((res: HttpResponse<ILocation[]>) => (this.locations = res.body || []));
    });
  }

  updateForm(bill: IBill): void {
    this.editForm.patchValue({
      id: bill.id,
      firstDay: bill.firstDay ? bill.firstDay.format(DATE_TIME_FORMAT) : null,
      lastDay: bill.lastDay ? bill.lastDay.format(DATE_TIME_FORMAT) : null,
      value: bill.value,
      paid: bill.paid,
      locationId: bill.locationId,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const bill = this.createFromForm();
    if (bill.id !== undefined) {
      this.subscribeToSaveResponse(this.billService.update(bill));
    } else {
      this.subscribeToSaveResponse(this.billService.create(bill));
    }
  }

  private createFromForm(): IBill {
    return {
      ...new Bill(),
      id: this.editForm.get(['id'])!.value,
      firstDay: this.editForm.get(['firstDay'])!.value ? moment(this.editForm.get(['firstDay'])!.value, DATE_TIME_FORMAT) : undefined,
      lastDay: this.editForm.get(['lastDay'])!.value ? moment(this.editForm.get(['lastDay'])!.value, DATE_TIME_FORMAT) : undefined,
      value: this.editForm.get(['value'])!.value,
      paid: this.editForm.get(['paid'])!.value,
      locationId: this.editForm.get(['locationId'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBill>>): void {
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
