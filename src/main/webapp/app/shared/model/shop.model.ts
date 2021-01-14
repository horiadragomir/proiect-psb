export interface IShop {
  id?: number;
  streetAddress?: string;
  phoneNumber?: string;
  weekHourStart?: number;
  weekHourEnd?: number;
  weekendHourStart?: number;
  weekendHourEnd?: number;
}

export class Shop implements IShop {
  constructor(
    public id?: number,
    public streetAddress?: string,
    public phoneNumber?: string,
    public weekHourStart?: number,
    public weekHourEnd?: number,
    public weekendHourStart?: number,
    public weekendHourEnd?: number
  ) {}
}
