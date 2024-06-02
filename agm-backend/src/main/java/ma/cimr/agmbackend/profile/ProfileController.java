package ma.cimr.agmbackend.profile;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.cimr.agmbackend.util.ApiResponse;
import ma.cimr.agmbackend.util.ApiResponseFormatter;

@RestController
@RequestMapping("profiles")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('Habilitations')")
@Tag(name = "Gestion des profils")
public class ProfileController {

	private final ProfileService profileService;

	@GetMapping
	public ResponseEntity<ApiResponse> getProfiles() {
		List<ProfileResponse> response = profileService.getProfiles();
		return ApiResponseFormatter.generateResponse(HttpStatus.OK, response);
	}

	@PostMapping
	public ResponseEntity<ApiResponse> createProfile(@Valid @RequestBody ProfileAddRequest profileAddRequest) {
		ProfileResponse response = profileService.createProfile(profileAddRequest);
		return ApiResponseFormatter.generateResponse(HttpStatus.CREATED, "Profil créé avec succès", response);
	}

	@PutMapping("/{profileId}/permissions/{permissionId}")
	public ResponseEntity<ApiResponse> addPermissionToProfile(@PathVariable Long profileId,
			@PathVariable Long permissionId) {
		ProfileResponse response = profileService.addPermissionToProfile(profileId, permissionId);
		return ApiResponseFormatter.generateResponse(HttpStatus.OK, "Permission ajoutée avec succès", response);
	}

	@DeleteMapping("/{profileId}/permissions/{permissionId}")
	public ResponseEntity<ApiResponse> removePermissionFromProfile(@PathVariable Long profileId,
			@PathVariable Long permissionId) {
		profileService.removePermissionFromProfile(profileId, permissionId);
		return ApiResponseFormatter.generateResponse(HttpStatus.OK, "Permission supprimée avec succès");
	}
}
