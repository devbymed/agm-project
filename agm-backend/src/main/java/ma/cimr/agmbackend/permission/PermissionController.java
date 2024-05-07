package ma.cimr.agmbackend.permission;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ma.cimr.agmbackend.util.ApiResponse;
import ma.cimr.agmbackend.util.ApiResponseFormatter;

@RestController
@RequestMapping("permissions")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('Habilitations')")
public class PermissionController {

	private final PermissionService permissionService;

	@GetMapping
	public ResponseEntity<ApiResponse> getPermissions() {
		List<PermissionResponse> permissions = permissionService.getAllPermissions();
		return ApiResponseFormatter.generateResponse(HttpStatus.OK, permissions);
	}
}
