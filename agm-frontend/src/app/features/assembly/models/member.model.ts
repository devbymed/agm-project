export interface Member {
  memberNumber: string;
  type: string;
  companyName: string;
  address1: string;
  address2?: string;
  city: string;
  phone1: string;
  phone2?: string;
  membershipDate: Date;
  workforce: number;
  title: string;
  dbrTrimester: number;
  dbrYear: number;
  dtrTrimester: number;
  dtrYear: number;
}
