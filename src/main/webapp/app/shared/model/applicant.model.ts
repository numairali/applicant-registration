export interface IApplicant {
  id?: number;
  first_name?: string;
  last_name?: string;
  email?: string;
  phone_number?: string;
}

export class Applicant implements IApplicant {
  constructor(
    public id?: number,
    public first_name?: string,
    public last_name?: string,
    public email?: string,
    public phone_number?: string
  ) {}
}
