package ma.cimr.agmbackend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.cimr.agmbackend.dto.request.ProfileAddRequest;
import ma.cimr.agmbackend.dto.request.ProfileEditRequest;
import ma.cimr.agmbackend.dto.response.ProfileResponse;
import ma.cimr.agmbackend.service.ProfileService;
import ma.cimr.agmbackend.util.ApiResponse;
import ma.cimr.agmbackend.util.ApiResponseFormatter;

@RestController
@RequestMapping("profiles")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('AUTHORIZATIONS')")
@Tag(name = "Gestion des profils")
public class ProfileController {

	private final ProfileService profileService;

	@GetMapping
	public ResponseEntity<ApiResponse> getProfiles() {
		List<ProfileResponse> response = profileService.getProfiles();
		return ApiResponseFormatter.generateResponse(HttpStatus.OK, response);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse> getProfile(@PathVariable Long id) {
		ProfileResponse response = profileService.getProfile(id);
		return ApiResponseFormatter.generateResponse(HttpStatus.OK, response);
	}

	@PostMapping
	public ResponseEntity<ApiResponse> createProfile(@Valid @RequestBody ProfileAddRequest profileAddRequest) {
		ProfileResponse response = profileService.createProfile(profileAddRequest);
		return ApiResponseFormatter.generateResponse(HttpStatus.CREATED, "Profil créé avec succès", response);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<ApiResponse> updateProfile(@PathVariable Long id,
			@Valid @RequestBody ProfileEditRequest profileEditRequest) {
		ProfileResponse response = profileService.updateProfile(id, profileEditRequest);
		return ApiResponseFormatter.generateResponse(HttpStatus.OK, "Profil mis à jour avec succès", response);
	}

	@PostMapping("/profiles/{profileId}/permissions")
	public ResponseEntity<ApiResponse> updateProfilePermissions(@PathVariable Long profileId,
			@RequestBody List<Long> permissionIds) {
		profileService.updatePermissions(profileId, permissionIds);
		return ApiResponseFormatter.generateResponse(HttpStatus.OK, "Permissions mises à jour avec succès");
	}
}
