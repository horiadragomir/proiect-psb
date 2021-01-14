import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IShop, Shop } from 'app/shared/model/shop.model';
import { ShopService } from './shop.service';

@Component({
  selector: 'jhi-shop-update',
  templateUrl: './shop-update.component.html',
})
export class ShopUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    streetAddress: [null, [Validators.required]],
    phoneNumber: [null, [Validators.required]],
    weekHourStart: [null, [Validators.required]],
    weekHourEnd: [null, [Validators.required]],
    weekendHourStart: [null, [Validators.required]],
    weekendHourEnd: [null, [Validators.required]],
  });

  constructor(protected shopService: ShopService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ shop }) => {
      this.updateForm(shop);
    });
  }

  updateForm(shop: IShop): void {
    this.editForm.patchValue({
      id: shop.id,
      streetAddress: shop.streetAddress,
      phoneNumber: shop.phoneNumber,
      weekHourStart: shop.weekHourStart,
      weekHourEnd: shop.weekHourEnd,
      weekendHourStart: shop.weekendHourStart,
      weekendHourEnd: shop.weekendHourEnd,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const shop = this.createFromForm();
    if (shop.id !== undefined) {
      this.subscribeToSaveResponse(this.shopService.update(shop));
    } else {
      this.subscribeToSaveResponse(this.shopService.create(shop));
    }
  }

  private createFromForm(): IShop {
    return {
      ...new Shop(),
      id: this.editForm.get(['id'])!.value,
      streetAddress: this.editForm.get(['streetAddress'])!.value,
      phoneNumber: this.editForm.get(['phoneNumber'])!.value,
      weekHourStart: this.editForm.get(['weekHourStart'])!.value,
      weekHourEnd: this.editForm.get(['weekHourEnd'])!.value,
      weekendHourStart: this.editForm.get(['weekendHourStart'])!.value,
      weekendHourEnd: this.editForm.get(['weekendHourEnd'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IShop>>): void {
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
}
