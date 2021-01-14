import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IIndex, Index } from 'app/shared/model/index.model';
import { IndexService } from './index.service';
import { IndexComponent } from './index.component';
import { IndexDetailComponent } from './index-detail.component';
import { IndexUpdateComponent } from './index-update.component';

@Injectable({ providedIn: 'root' })
export class IndexResolve implements Resolve<IIndex> {
  constructor(private service: IndexService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IIndex> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((index: HttpResponse<Index>) => {
          if (index.body) {
            return of(index.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Index());
  }
}

export const indexRoute: Routes = [
  {
    path: '',
    component: IndexComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'easypayApp.index.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: IndexDetailComponent,
    resolve: {
      index: IndexResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'easypayApp.index.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: IndexUpdateComponent,
    resolve: {
      index: IndexResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'easypayApp.index.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: IndexUpdateComponent,
    resolve: {
      index: IndexResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'easypayApp.index.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
