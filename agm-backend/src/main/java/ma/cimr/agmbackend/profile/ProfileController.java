package ma.cimr.agmbackend.profile;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

	@PutMapping("/{profileId}/features/{featureName}")
	public ResponseEntity<ApiResponse> updateProfileFeature(
			@PathVariable Long profileId,
			@PathVariable String featureName,
			@RequestBody boolean isEnabled) {
		profileService.updateProfileFeature(profileId, featureName, isEnabled);
		ProfileResponse updatedProfile = profileService.getProfile(profileId);
		return ApiResponseFormatter.generateResponse(HttpStatus.OK, "Fonctionnalité mise à jour avec succès",
				updatedProfile);
	}

	// @GetMapping("/{id}/features")
	// public ResponseEntity<ApiResponse> getProfileFeatures(@PathVariable Long id)
	// {
	// Set<Feature> features = profileService.getProfileFeatures(id);
	// return ApiResponseFormatter.generateResponse(HttpStatus.OK, features);
	// }

	// @PatchMapping("/{id}/features")
	// public ResponseEntity<ApiResponse> updateProfileFeatures(@PathVariable Long
	// id,
	// @RequestBody Set<Feature> features) {
	// profileService.updateProfileFeatures(id, features);
	// return ApiResponseFormatter.generateResponse(HttpStatus.OK, "Fonctionnalités
	// du profil mises à jour avec succès");
	// }
}
