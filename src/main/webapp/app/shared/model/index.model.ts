export interface IIndex {
  id?: number;
  value?: number;
  month?: number;
  year?: number;
  locationId?: number;
}

export class Index implements IIndex {
  constructor(public id?: number, public value?: number, public month?: number, public year?: number, public locationId?: number) {}
}
