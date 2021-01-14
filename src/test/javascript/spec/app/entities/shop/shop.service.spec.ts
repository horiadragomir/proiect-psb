import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { ShopService } from 'app/entities/shop/shop.service';
import { IShop, Shop } from 'app/shared/model/shop.model';

describe('Service Tests', () => {
  describe('Shop Service', () => {
    let injector: TestBed;
    let service: ShopService;
    let httpMock: HttpTestingController;
    let elemDefault: IShop;
    let expectedResult: IShop | IShop[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(ShopService);
      httpMock = injector.get(HttpTestingController);

      elemDefault = new Shop(0, 'AAAAAAA', 'AAAAAAA', 0, 0, 0, 0);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Shop', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Shop()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Shop', () => {
        const returnedFromService = Object.assign(
          {
            streetAddress: 'BBBBBB',
            phoneNumber: 'BBBBBB',
            weekHourStart: 1,
            weekHourEnd: 1,
            weekendHourStart: 1,
            weekendHourEnd: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Shop', () => {
        const returnedFromService = Object.assign(
          {
            streetAddress: 'BBBBBB',
            phoneNumber: 'BBBBBB',
            weekHourStart: 1,
            weekHourEnd: 1,
            weekendHourStart: 1,
            weekendHourEnd: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Shop', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
