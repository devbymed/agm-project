export interface Member {
  id: number;
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
  agentFullName: string;
  status: string;
  assignmentDate: Date;
  editionDate: Date;
  invitationLetterPath: string;
  attendanceSheetPath: string;
  proxyPath: string;
  isSelected: boolean;
}
