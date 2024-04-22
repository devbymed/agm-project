package ma.cimr.agmbackend.user;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.cimr.agmbackend.util.ApiResponseFormatter;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
@Tag(name = "Gestion des utilisateurs")
public class UserController {

	private final UserService userService;

	// @Operation(summary = "Créer un utilisateur")
	// @ApiResponses(value = { @ApiResponse(responseCode = "201", description =
	// "L'utilisateur a été créé avec succès"),
	// @ApiResponse(responseCode = "401", description = "Identifiant et/ou mot de
	// passe incorrect(s)") })
	@PostMapping("/create")
	public ResponseEntity<Map<String, Object>> createUser(@Valid @RequestBody UserAddRequest userAddRequest) {
		UserResponse createdUser = userService.createUser(userAddRequest);
		return ApiResponseFormatter.generateResponse(HttpStatus.CREATED, "Utilisateur créé avec succès", createdUser);
	}

	@PatchMapping("/update/{id}")
	public ResponseEntity<Map<String, Object>> updateUser(@PathVariable Long id,
			@RequestBody UserEditRequest userEditRequest) {
		UserResponse updatedUser = userService.updateUser(id, userEditRequest);
		return ApiResponseFormatter.generateResponse(HttpStatus.OK, "Utilisateur mis à jour avec succès", updatedUser);
	}
}
