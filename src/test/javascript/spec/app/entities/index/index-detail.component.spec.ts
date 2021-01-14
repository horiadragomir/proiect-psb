import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EasypayTestModule } from '../../../test.module';
import { IndexDetailComponent } from 'app/entities/index/index-detail.component';
import { Index } from 'app/shared/model/index.model';

describe('Component Tests', () => {
  describe('Index Management Detail Component', () => {
    let comp: IndexDetailComponent;
    let fixture: ComponentFixture<IndexDetailComponent>;
    const route = ({ data: of({ index: new Index(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [EasypayTestModule],
        declarations: [IndexDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(IndexDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(IndexDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load index on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.index).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
