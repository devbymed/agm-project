package ma.cimr.agmbackend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.cimr.agmbackend.dto.request.AssemblyCreateRequest;
import ma.cimr.agmbackend.dto.response.AssemblyResponse;
import ma.cimr.agmbackend.service.AssemblyService;
import ma.cimr.agmbackend.util.ApiResponse;
import ma.cimr.agmbackend.util.ApiResponseFormatter;

@RestController
@RequestMapping("assemblies")
@RequiredArgsConstructor
// @PreAuthorize("hasAuthority('USER_MANAGEMENT')")
// @Tag(name = "Gestion des utilisateurs")
public class AssemblyController {

	private final AssemblyService assemblyService;

	// @GetMapping
	// public ResponseEntity<ApiResponse> getAssemblies() {
	// List<AssemblyResponse> assemblies = assemblyService.getAssemblies();
	// return ApiResponseFormatter.generateResponse(HttpStatus.OK, assemblies);
	// }

	@GetMapping("/current")
	public ResponseEntity<ApiResponse> getCurrentAssembly() {
		AssemblyResponse currentAssembly = assemblyService.getCurrentAssembly();
		return ApiResponseFormatter.generateResponse(HttpStatus.OK, currentAssembly);
	}

	// @GetMapping("/{id}")
	// public ResponseEntity<ApiResponse> getAssembly(@PathVariable Long id) {
	// AssemblyResponse assembly = assemblyService.getAssembly(id);
	// return ApiResponseFormatter.generateResponse(HttpStatus.OK, assembly);
	// }

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

	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse> updateAssembly(@PathVariable Long id,
			@ModelAttribute @Valid AssemblyCreateRequest request,
			@RequestParam("routeSheet") MultipartFile routeSheet,
			@RequestParam("invitationLetter") MultipartFile invitationLetter,
			@RequestParam("attendanceSheet") MultipartFile attendanceSheet,
			@RequestParam("proxy") MultipartFile proxy,
			@RequestParam("attendanceForm") MultipartFile attendanceForm) {
		AssemblyResponse updatedAssembly = assemblyService.updateAssembly(id, request, routeSheet, invitationLetter,
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
}
