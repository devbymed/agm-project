package ma.cimr.agmbackend.permission;

import java.util.List;

public interface PermissionService {

	List<PermissionResponse> getPermissionsHierarchy();

	// PermissionResponse getPermissionById(Long id);
}
