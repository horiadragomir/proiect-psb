import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IIndex } from 'app/shared/model/index.model';

type EntityResponseType = HttpResponse<IIndex>;
type EntityArrayResponseType = HttpResponse<IIndex[]>;

@Injectable({ providedIn: 'root' })
export class IndexService {
  public resourceUrl = SERVER_API_URL + 'api/indices';

  constructor(protected http: HttpClient) {}

  create(index: IIndex): Observable<EntityResponseType> {
    return this.http.post<IIndex>(this.resourceUrl, index, { observe: 'response' });
  }

  update(index: IIndex): Observable<EntityResponseType> {
    return this.http.put<IIndex>(this.resourceUrl, index, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IIndex>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IIndex[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
