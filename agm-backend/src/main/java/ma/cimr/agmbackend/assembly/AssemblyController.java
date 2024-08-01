package ma.cimr.agmbackend.assembly;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.cimr.agmbackend.util.ApiResponse;
import ma.cimr.agmbackend.util.ApiResponseFormatter;

@RestController
@RequestMapping("assemblies")
@RequiredArgsConstructor
// @PreAuthorize("hasAuthority('USER_MANAGEMENT')")
// @Tag(name = "Gestion des utilisateurs")
public class AssemblyController {

	private final AssemblyService assemblyService;

	@GetMapping("/current")
	public ResponseEntity<ApiResponse> getCurrentAssembly() {
		AssemblyResponse currentAssembly = assemblyService.getCurrentAssembly();
		return ApiResponseFormatter.generateResponse(HttpStatus.OK, currentAssembly);
	}

	@PostMapping
	public ResponseEntity<ApiResponse> createAssembly(@ModelAttribute @Valid AssemblyCreateRequest request,
			@RequestParam("routeSheet") MultipartFile routeSheet,
			@RequestParam("invitationLetter") MultipartFile invitationLetter,
			@RequestParam("attendanceSheet") MultipartFile attendanceSheet,
			@RequestParam("proxy") MultipartFile proxy,
			@RequestParam("attendanceForm") MultipartFile attendanceForm) {
		AssemblyResponse createdAssembly = assemblyService.createAssembly(request, routeSheet, invitationLetter,
				attendanceSheet, proxy, attendanceForm);
		return ApiResponseFormatter.generateResponse(HttpStatus.CREATED, "Assemblée créée avec succès", createdAssembly);
	}

	@PatchMapping("/current")
	public ResponseEntity<ApiResponse> updateCurrentAssembly(
			@Valid @ModelAttribute AssemblyEditRequest request,
			@RequestParam(value = "routeSheet", required = false) MultipartFile routeSheet,
			@RequestParam(value = "invitationLetter", required = false) MultipartFile invitationLetter,
			@RequestParam(value = "attendanceSheet", required = false) MultipartFile attendanceSheet,
			@RequestParam(value = "proxy", required = false) MultipartFile proxy,
			@RequestParam(value = "attendanceForm", required = false) MultipartFile attendanceForm) {
		AssemblyResponse updatedAssembly = assemblyService.updateCurrentAssembly(request, routeSheet, invitationLetter,
				attendanceSheet, proxy, attendanceForm);
		return ApiResponseFormatter.generateResponse(HttpStatus.OK, "Assemblée mise à jour avec succès", updatedAssembly);
	}

	@DeleteMapping("/current")
	public ResponseEntity<ApiResponse> deleteCurrentAssembly() {
		assemblyService.deleteCurrentAssembly();
		return ApiResponseFormatter.generateResponse(HttpStatus.OK, "Assemblée en cours supprimée avec succès");
	}

	@PatchMapping("/current/close")
	public ResponseEntity<ApiResponse> closeCurrentAssembly() {
		AssemblyResponse closedAssembly = assemblyService.closeCurrentAssembly();
		return ApiResponseFormatter.generateResponse(HttpStatus.OK, "Assemblée en cours clôturée avec succès",
				closedAssembly);
	}

	@GetMapping("/current-exists")
	public ResponseEntity<Boolean> hasCurrentAssembly() {
		boolean exists = assemblyService.hasCurrentAssembly();
		return ResponseEntity.ok(exists);
	}
}
