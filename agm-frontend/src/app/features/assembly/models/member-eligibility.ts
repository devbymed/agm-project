import { Member } from './member.model';

export interface MemberEligibility {
  memberNumber: string;
  companyName: string;
  eligibilityStatus: string;
  MemberDetails: Member;
}
