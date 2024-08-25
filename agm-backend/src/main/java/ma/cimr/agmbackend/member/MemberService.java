package ma.cimr.agmbackend.member;

import java.util.List;

public interface MemberService {
	// List<MemberResponse> getEligibleMembersForAssembly();
	List<MemberResponse> getEligibleMembers();

	MemberResponse updateMember(String memberNumber, MemberEditRequest memberEditRequest);

	MemberEligibilityResponse searchMemberEligibility(String memberNumber);
}
