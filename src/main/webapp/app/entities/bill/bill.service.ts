import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IBill } from 'app/shared/model/bill.model';

type EntityResponseType = HttpResponse<IBill>;
type EntityArrayResponseType = HttpResponse<IBill[]>;

@Injectable({ providedIn: 'root' })
export class BillService {
  public resourceUrl = SERVER_API_URL + 'api/bills';

  constructor(protected http: HttpClient) {}

  create(bill: IBill): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(bill);
    return this.http
      .post<IBill>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(bill: IBill): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(bill);
    return this.http
      .put<IBill>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IBill>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IBill[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(bill: IBill): IBill {
    const copy: IBill = Object.assign({}, bill, {
      firstDay: bill.firstDay && bill.firstDay.isValid() ? bill.firstDay.toJSON() : undefined,
      lastDay: bill.lastDay && bill.lastDay.isValid() ? bill.lastDay.toJSON() : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.firstDay = res.body.firstDay ? moment(res.body.firstDay) : undefined;
      res.body.lastDay = res.body.lastDay ? moment(res.body.lastDay) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((bill: IBill) => {
        bill.firstDay = bill.firstDay ? moment(bill.firstDay) : undefined;
        bill.lastDay = bill.lastDay ? moment(bill.lastDay) : undefined;
      });
    }
    return res;
  }
}
