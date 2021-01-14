import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IIndex } from 'app/shared/model/index.model';
import { IndexService } from './index.service';

@Component({
  templateUrl: './index-delete-dialog.component.html',
})
export class IndexDeleteDialogComponent {
  index?: IIndex;

  constructor(protected indexService: IndexService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.indexService.delete(id).subscribe(() => {
      this.eventManager.broadcast('indexListModification');
      this.activeModal.close();
    });
  }
}
