export interface IClient {
  id?: number;
  email?: string;
  password?: string;
  firstName?: string;
  lastName?: string;
  phoneNumber?: string;
}

export class Client implements IClient {
  constructor(
    public id?: number,
    public email?: string,
    public password?: string,
    public firstName?: string,
    public lastName?: string,
    public phoneNumber?: string
  ) {}
}
