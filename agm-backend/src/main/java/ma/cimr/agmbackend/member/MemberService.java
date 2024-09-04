package ma.cimr.agmbackend.member;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

public interface MemberService {
	// List<MemberResponse> getEligibleMembersForAssembly();
	List<MemberResponse> getEligibleMembers();

	MemberResponse getMemberById(Long memberId);

	MemberResponse updateMember(String memberNumber, MemberEditRequest memberEditRequest);

	MemberEligibilityResponse searchMemberEligibility(String memberNumber);

	void assignMembersToAgent(List<String> memberNumbers, Long agentId);

	void autoAssignMembers();

	void changeAgentForMember(String memberNumber, Long newAgentId);

	Map<String, String> generateDocumentsForMember(Long memberId) throws FileNotFoundException;
}
