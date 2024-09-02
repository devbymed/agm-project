package ma.cimr.agmbackend.member;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ma.cimr.agmbackend.util.ApiResponse;
import ma.cimr.agmbackend.util.ApiResponseFormatter;

@RestController
@RequestMapping("members")
@RequiredArgsConstructor
public class MemberController {
	private final MemberService memberService;

	@GetMapping
	public ResponseEntity<ApiResponse> getEligibleMembersForAssembly() {
		List<MemberResponse> eligibleMembers = memberService.getEligibleMembers();
		return ApiResponseFormatter.generateResponse(HttpStatus.OK, eligibleMembers);
	}

	@PatchMapping("/{memberNumber}")
	public ResponseEntity<ApiResponse> updateMember(
			@PathVariable String memberNumber,
			@RequestBody MemberEditRequest memberEditRequest) {
		MemberResponse updatedMember = memberService.updateMember(memberNumber, memberEditRequest);
		return ApiResponseFormatter.generateResponse(HttpStatus.OK, "Adhérent mis à jour avec succès", updatedMember);
	}

	@GetMapping("/search/{memberNumber}")
	public ResponseEntity<ApiResponse> searchMemberEligibility(@PathVariable String memberNumber) {
		MemberEligibilityResponse eligibilityResponse = memberService.searchMemberEligibility(memberNumber);
		return ApiResponseFormatter.generateResponse(HttpStatus.OK, eligibilityResponse);
	}

	@PostMapping("/assign")
	public ResponseEntity<ApiResponse> assignMembersToAgent(@RequestBody AssignMembersRequest request) {
		memberService.assignMembersToAgent(request.getMemberNumbers(), request.getAgentId());
		return ApiResponseFormatter.generateResponse(HttpStatus.OK, "Affectation manuelle réussie");
	}

	@PostMapping("/auto-assign")
	public ResponseEntity<ApiResponse> autoAssignMembers() {
		memberService.autoAssignMembers();
		return ApiResponseFormatter.generateResponse(HttpStatus.OK, "Affectation automatique réussie");
	}

	@PostMapping("/generate-documents")
	public ResponseEntity<ApiResponse> generateDocuments(@RequestBody GenerateMemberDocumentsRequest request)
			throws IOException {
		memberService.generateDocumentsForMember(request.getMemberId());
		return ApiResponseFormatter.generateResponse(HttpStatus.OK, "Documents générés avec succès");
	}
}
