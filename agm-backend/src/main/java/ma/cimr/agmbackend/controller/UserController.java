package ma.cimr.agmbackend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.cimr.agmbackend.dto.request.UserCreateRequest;
import ma.cimr.agmbackend.dto.response.UserCreateResponse;
import ma.cimr.agmbackend.mapper.UserMapper;
import ma.cimr.agmbackend.model.User;
import ma.cimr.agmbackend.service.UserService;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
@Tag(name = "Gestion des utilisateurs")
public class UserController {

	private final UserService userService;
	private final UserMapper userMapper;

	@Operation(summary = "Créer un utilisateur")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "L'utilisateur a été créé avec succès"),
			@ApiResponse(responseCode = "400", description = "Les données fournies ne sont pas valides"),
			@ApiResponse(responseCode = "401", description = "Les informations d'authentification sont incorrectes"),
			@ApiResponse(responseCode = "500", description = "Une erreur inattendue s'est produite")
	})
	@PostMapping("/create")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<UserCreateResponse> createUser(@RequestBody @Valid UserCreateRequest userCreateRequest) {
		User user = userService.createUser(userCreateRequest);
		UserCreateResponse userCreateResponse = userMapper.toUserCreateResponse(user);
		userCreateResponse.setMessage("L'utilisateur a été créé avec succès");
		return ResponseEntity.status(HttpStatus.CREATED).body(userCreateResponse);
	}
}
