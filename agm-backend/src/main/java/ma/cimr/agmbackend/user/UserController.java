package ma.cimr.agmbackend.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import ma.cimr.agmbackend.util.ApiResponse;
import ma.cimr.agmbackend.util.ApiResponseFormatter;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
@Tag(name = "Gestion des utilisateurs")
public class UserController {

	private final UserService userService;

	@GetMapping
	public ResponseEntity<ApiResponse> getUsers() {
		return ApiResponseFormatter.generateResponse(HttpStatus.OK, userService.getUsers());
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse> getUser(@PathVariable Long id) {
		UserResponse user = userService.getUser(id);
		return ApiResponseFormatter.generateResponse(HttpStatus.OK, user);
	}

	@PostMapping
	public ResponseEntity<ApiResponse> createUser(@Valid @RequestBody UserAddRequest userAddRequest) {
		UserResponse createdUser = userService.createUser(userAddRequest);
		return ApiResponseFormatter.generateResponse(HttpStatus.CREATED, "Utilisateur créé avec succès", createdUser);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<ApiResponse> updateUser(@PathVariable Long id,
			@RequestBody UserEditRequest userEditRequest) {
		UserResponse updatedUser = userService.updateUser(id, userEditRequest);
		return ApiResponseFormatter.generateResponse(HttpStatus.OK, "Utilisateur mis à jour avec succès", updatedUser);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long id) {
		userService.deleteUser(id);
		return ApiResponseFormatter.generateResponse(HttpStatus.OK, "Utilisateur supprimé avec succès");
	}
}
