import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IShop } from 'app/shared/model/shop.model';

type EntityResponseType = HttpResponse<IShop>;
type EntityArrayResponseType = HttpResponse<IShop[]>;

@Injectable({ providedIn: 'root' })
export class ShopService {
  public resourceUrl = SERVER_API_URL + 'api/shops';

  constructor(protected http: HttpClient) {}

  create(shop: IShop): Observable<EntityResponseType> {
    return this.http.post<IShop>(this.resourceUrl, shop, { observe: 'response' });
  }

  update(shop: IShop): Observable<EntityResponseType> {
    return this.http.put<IShop>(this.resourceUrl, shop, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IShop>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IShop[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
