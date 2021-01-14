import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IPayment, Payment } from 'app/shared/model/payment.model';
import { PaymentService } from './payment.service';
import { IBill } from 'app/shared/model/bill.model';
import { BillService } from 'app/entities/bill/bill.service';
import { IShop } from 'app/shared/model/shop.model';
import { ShopService } from 'app/entities/shop/shop.service';

type SelectableEntity = IBill | IShop;

@Component({
  selector: 'jhi-payment-update',
  templateUrl: './payment-update.component.html',
})
export class PaymentUpdateComponent implements OnInit {
  isSaving = false;
  bills: IBill[] = [];
  shops: IShop[] = [];

  editForm = this.fb.group({
    id: [],
    day: [null, [Validators.required]],
    billId: [],
    shopId: [],
  });

  constructor(
    protected paymentService: PaymentService,
    protected billService: BillService,
    protected shopService: ShopService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ payment }) => {
      if (!payment.id) {
        const today = moment().startOf('day');
        payment.day = today;
      }

      this.updateForm(payment);

      this.billService
        .query({ 'paymentId.specified': 'false' })
        .pipe(
          map((res: HttpResponse<IBill[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: IBill[]) => {
          if (!payment.billId) {
            this.bills = resBody;
          } else {
            this.billService
              .find(payment.billId)
              .pipe(
                map((subRes: HttpResponse<IBill>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: IBill[]) => (this.bills = concatRes));
          }
        });

      this.shopService
        .query({ 'paymentId.specified': 'false' })
        .pipe(
          map((res: HttpResponse<IShop[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: IShop[]) => {
          if (!payment.shopId) {
            this.shops = resBody;
          } else {
            this.shopService
              .find(payment.shopId)
              .pipe(
                map((subRes: HttpResponse<IShop>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: IShop[]) => (this.shops = concatRes));
          }
        });
    });
  }

  updateForm(payment: IPayment): void {
    this.editForm.patchValue({
      id: payment.id,
      day: payment.day ? payment.day.format(DATE_TIME_FORMAT) : null,
      billId: payment.billId,
      shopId: payment.shopId,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const payment = this.createFromForm();
    if (payment.id !== undefined) {
      this.subscribeToSaveResponse(this.paymentService.update(payment));
    } else {
      this.subscribeToSaveResponse(this.paymentService.create(payment));
    }
  }

  private createFromForm(): IPayment {
    return {
      ...new Payment(),
      id: this.editForm.get(['id'])!.value,
      day: this.editForm.get(['day'])!.value ? moment(this.editForm.get(['day'])!.value, DATE_TIME_FORMAT) : undefined,
      billId: this.editForm.get(['billId'])!.value,
      shopId: this.editForm.get(['shopId'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPayment>>): void {
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

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }
}
