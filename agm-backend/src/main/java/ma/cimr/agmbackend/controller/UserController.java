package ma.cimr.agmbackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ma.cimr.agmbackend.dto.request.UserCreateRequest;
import ma.cimr.agmbackend.dto.response.UserCreateResponse;
import ma.cimr.agmbackend.service.UserService;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@PostMapping("/create")
	public ResponseEntity<UserCreateResponse> createUser(@RequestBody UserCreateRequest userCreateRequest) {
		return ResponseEntity.ok(userService.createUser(userCreateRequest));
	}

	@GetMapping("/all")
	public ResponseEntity<String> getAllUsers() {
		return ResponseEntity.ok("All users");
	}
}
