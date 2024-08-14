package ma.cimr.agmbackend.member;

import java.util.List;

public interface MemberService {
	// List<MemberResponse> getEligibleMembersForAssembly();
	List<Member> extractEligibleMembers();
}
