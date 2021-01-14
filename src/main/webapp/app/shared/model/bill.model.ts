import { Moment } from 'moment';

export interface IBill {
  id?: number;
  firstDay?: Moment;
  lastDay?: Moment;
  value?: number;
  paid?: boolean;
  locationId?: number;
}

export class Bill implements IBill {
  constructor(
    public id?: number,
    public firstDay?: Moment,
    public lastDay?: Moment,
    public value?: number,
    public paid?: boolean,
    public locationId?: number
  ) {
    this.paid = this.paid || false;
  }
}
