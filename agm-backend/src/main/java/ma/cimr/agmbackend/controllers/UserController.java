package ma.cimr.agmbackend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ma.cimr.agmbackend.dto.UserCreationReqDTO;
import ma.cimr.agmbackend.models.User;
import ma.cimr.agmbackend.services.UserService;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@PostMapping("/create")
	public ResponseEntity<User> createUser(@RequestBody UserCreationReqDTO userCreationRequest) {
		return ResponseEntity.ok(userService.createUser(userCreationRequest));
	}
}
