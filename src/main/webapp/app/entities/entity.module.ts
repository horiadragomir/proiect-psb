import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'client',
        loadChildren: () => import('./client/client.module').then(m => m.EasypayClientModule),
      },
      {
        path: 'location',
        loadChildren: () => import('./location/location.module').then(m => m.EasypayLocationModule),
      },
      {
        path: 'index',
        loadChildren: () => import('./index/index.module').then(m => m.EasypayIndexModule),
      },
      {
        path: 'shop',
        loadChildren: () => import('./shop/shop.module').then(m => m.EasypayShopModule),
      },
      {
        path: 'bill',
        loadChildren: () => import('./bill/bill.module').then(m => m.EasypayBillModule),
      },
      {
        path: 'payment',
        loadChildren: () => import('./payment/payment.module').then(m => m.EasypayPaymentModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EasypayEntityModule {}
