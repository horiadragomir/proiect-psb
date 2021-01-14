import { Moment } from 'moment';

export interface IPayment {
  id?: number;
  day?: Moment;
  billId?: number;
  shopId?: number;
}

export class Payment implements IPayment {
  constructor(public id?: number, public day?: Moment, public billId?: number, public shopId?: number) {}
}
