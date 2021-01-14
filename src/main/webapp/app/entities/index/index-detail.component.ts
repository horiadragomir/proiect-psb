import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IIndex } from 'app/shared/model/index.model';

@Component({
  selector: 'jhi-index-detail',
  templateUrl: './index-detail.component.html',
})
export class IndexDetailComponent implements OnInit {
  index: IIndex | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ index }) => (this.index = index));
  }

  previousState(): void {
    window.history.back();
  }
}
