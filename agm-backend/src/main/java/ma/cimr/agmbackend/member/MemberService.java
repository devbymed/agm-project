package ma.cimr.agmbackend.member;

import java.io.FileNotFoundException;
import java.util.List;

public interface MemberService {
	// List<MemberResponse> getEligibleMembersForAssembly();
	List<MemberResponse> getEligibleMembers();

	MemberResponse updateMember(String memberNumber, MemberEditRequest memberEditRequest);

	MemberEligibilityResponse searchMemberEligibility(String memberNumber);

	void assignMembersToAgent(List<String> memberNumbers, Long agentId);

	void autoAssignMembers();

	void generateDocumentsForMember(Long memberId) throws FileNotFoundException;
}
