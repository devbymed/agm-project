package ma.cimr.agmbackend.member;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ma.cimr.agmbackend.util.ApiResponse;
import ma.cimr.agmbackend.util.ApiResponseFormatter;

@RestController
@RequestMapping("members")
@RequiredArgsConstructor
public class MemberController {

	@Value("${file.uploads.output-path}")
	private String baseUploadPath;

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
		Map<String, String> generatedDocuments = memberService.generateDocumentsForMember(request.getMemberId());
		return ApiResponseFormatter.generateResponse(HttpStatus.OK, "Documents générés avec succès",
				generatedDocuments);
	}

	@GetMapping("/{memberId}/generated-documents")
	public ResponseEntity<ApiResponse> getGeneratedDocuments(@PathVariable Long memberId) {
		MemberResponse member = memberService.getMemberById(memberId);
		Map<String, String> generatedDocuments = new HashMap<>();
		generatedDocuments.put("invitationLetter", member.getInvitationLetterPath());
		generatedDocuments.put("attendanceSheet", member.getAttendanceSheetPath());
		generatedDocuments.put("proxy", member.getProxyPath());
		return ApiResponseFormatter.generateResponse(HttpStatus.OK, generatedDocuments);
	}

	@GetMapping("/download")
	public ResponseEntity<Resource> downloadFile(@RequestParam String filepath) {
		try {
			Path file = Paths.get(filepath);
			Resource resource = new UrlResource(file.toUri());

			if (resource.exists() || resource.isReadable()) {
				return ResponseEntity.ok()
						.contentType(MediaType.parseMediaType(
								"application/vnd.openxmlformats-officedocument.wordprocessingml.document"))
						.header(HttpHeaders.CONTENT_DISPOSITION,
								"attachment; filename=\"" + file.getFileName().toString() + "\"")
						.body(resource);
			} else {
				throw new RuntimeException("Could not read the file!");
			}
		} catch (Exception e) {
			throw new RuntimeException("Error: " + e.getMessage());
		}
	}
}
