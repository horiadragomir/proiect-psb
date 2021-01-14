import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EasypaySharedModule } from 'app/shared/shared.module';
import { IndexComponent } from './index.component';
import { IndexDetailComponent } from './index-detail.component';
import { IndexUpdateComponent } from './index-update.component';
import { IndexDeleteDialogComponent } from './index-delete-dialog.component';
import { indexRoute } from './index.route';

@NgModule({
  imports: [EasypaySharedModule, RouterModule.forChild(indexRoute)],
  declarations: [IndexComponent, IndexDetailComponent, IndexUpdateComponent, IndexDeleteDialogComponent],
  entryComponents: [IndexDeleteDialogComponent],
})
export class EasypayIndexModule {}
