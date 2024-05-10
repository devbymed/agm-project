package ma.cimr.agmbackend.user;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.cimr.agmbackend.util.ApiResponse;
import ma.cimr.agmbackend.util.ApiResponseFormatter;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('Gestion utilisateurs')")
@Tag(name = "Gestion des utilisateurs")
public class UserController {

	private final UserService userService;

	@GetMapping
	public ResponseEntity<ApiResponse> getUsers(@RequestParam Optional<Integer> page,
			@RequestParam Optional<Integer> size) {
		Page<UserResponse> response = userService.getUsers(page.orElse(0), size.orElse(20));
		return ApiResponseFormatter.generateResponse(HttpStatus.OK, response);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse> getUser(@PathVariable Long id) {
		UserResponse user = userService.getUser(id);
		return ApiResponseFormatter.generateResponse(HttpStatus.OK, user);
	}

	@PostMapping
	public ResponseEntity<ApiResponse> createUser(@Valid @RequestBody UserAddRequest userAddRequest)
			throws MessagingException {
		UserResponse createdUser = userService.createUser(userAddRequest);
		return ApiResponseFormatter.generateResponse(HttpStatus.CREATED, "Utilisateur créé avec succès", createdUser);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<ApiResponse> updateUser(@PathVariable Long id,
			@Valid @RequestBody UserEditRequest userEditRequest) {
		UserResponse updatedUser = userService.updateUser(id, userEditRequest);
		return ApiResponseFormatter.generateResponse(HttpStatus.OK, "Utilisateur mis à jour avec succès", updatedUser);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long id) {
		userService.deleteUser(id);
		return ApiResponseFormatter.generateResponse(HttpStatus.OK, "Utilisateur supprimé avec succès");
	}
}
