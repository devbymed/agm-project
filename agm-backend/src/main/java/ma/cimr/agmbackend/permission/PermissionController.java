package ma.cimr.agmbackend.permission;

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

import lombok.RequiredArgsConstructor;
import ma.cimr.agmbackend.util.ApiResponse;
import ma.cimr.agmbackend.util.ApiResponseFormatter;

@RestController
@RequestMapping("permissions")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('AUTHORIZATIONS')")
public class PermissionController {

	private final PermissionService permissionService;

	// @GetMapping
	// public ResponseEntity<ApiResponse> getPermissions() {
	// List<PermissionResponse> permissions = permissionService.getAllPermissions();
	// return ApiResponseFormatter.generateResponse(HttpStatus.OK, permissions);
	// }

	@GetMapping
	public ResponseEntity<ApiResponse> getPermissions() {
		List<PermissionResponse> permissions = permissionService.getAllPermissions();
		return ApiResponseFormatter.generateResponse(HttpStatus.OK, permissions);
	}

	@GetMapping("/hierarchy")
	public ResponseEntity<ApiResponse> getPermissionsWithHierarchy() {
		List<PermissionResponse> permissions = permissionService.getPermissionsWithHierarchy();
		return ApiResponseFormatter.generateResponse(HttpStatus.OK, permissions);
	}

	@PostMapping
	public Permission createPermission(@RequestBody Permission permission) {
		return permissionService.createPermission(permission);
	}

	@PutMapping("/{id}")
	public Permission updatePermission(@PathVariable Long id, @RequestBody Permission permissionDetails) {
		return permissionService.updatePermission(id, permissionDetails);
	}

	@DeleteMapping("/{id}")
	public void deletePermission(@PathVariable Long id) {
		permissionService.deletePermission(id);
	}
}
