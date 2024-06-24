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
@PreAuthorize("hasAuthority('AUTHORIZATIONS')")
public class PermissionController {

	private final PermissionService permissionService;

	@GetMapping("/hierarchy")
	public ResponseEntity<ApiResponse> getPermissionsHierarchy() {
		List<PermissionResponse> permissions = permissionService.getPermissionsHierarchy();
		return ApiResponseFormatter.generateResponse(HttpStatus.OK, permissions);
	}

	// @GetMapping("/{id}")
	// public ResponseEntity<ApiResponse> getPermission(@PathVariable Long id) {
	// PermissionResponse permission = permissionService.getPermissionById(id);
	// return ApiResponseFormatter.generateResponse(HttpStatus.OK, permission);
	// }
}
