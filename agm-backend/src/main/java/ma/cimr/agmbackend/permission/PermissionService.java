package ma.cimr.agmbackend.permission;

import java.util.List;

public interface PermissionService {

	// List<PermissionResponse> getAllPermissions();

	List<PermissionResponse> getAllPermissions();

	List<PermissionResponse> getPermissionsWithHierarchy();

	Permission createPermission(Permission permission);

	Permission updatePermission(Long id, Permission permissionDetails);

	void deletePermission(Long id);
}
