import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { EasypayTestModule } from '../../../test.module';
import { IndexUpdateComponent } from 'app/entities/index/index-update.component';
import { IndexService } from 'app/entities/index/index.service';
import { Index } from 'app/shared/model/index.model';

describe('Component Tests', () => {
  describe('Index Management Update Component', () => {
    let comp: IndexUpdateComponent;
    let fixture: ComponentFixture<IndexUpdateComponent>;
    let service: IndexService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [EasypayTestModule],
        declarations: [IndexUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(IndexUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(IndexUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(IndexService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Index(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Index();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
