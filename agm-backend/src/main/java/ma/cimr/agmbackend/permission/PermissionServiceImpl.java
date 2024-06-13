package ma.cimr.agmbackend.permission;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ma.cimr.agmbackend.exception.ApiException;
import ma.cimr.agmbackend.exception.ApiExceptionCodes;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

	private final PermissionRepository permissionRepository;
	private final PermissionMapper permissionMapper;

	// public List<PermissionResponse> getAllPermissions() {
	// return permissionRepository.findAll().stream()
	// .map(permissionMapper::toPermissionResponse)
	// .collect(Collectors.toList());
	// }

	@Override
	public List<PermissionResponse> getAllPermissions() {
		List<Permission> permissions = permissionRepository.findAll();
		return permissionMapper.toPermissionResponseList(permissions);
	}

	@Override
	public List<PermissionResponse> getPermissionsWithHierarchy() {
		List<Permission> allPermissions = permissionRepository.findAll();
		List<Permission> rootPermissions = allPermissions.stream()
				.filter(permission -> permission.getParent() == null)
				.collect(Collectors.toList());
		return permissionMapper.toPermissionResponseList(rootPermissions);
	}

	public Permission createPermission(Permission permission) {
		return permissionRepository.save(permission);
	}

	public Permission updatePermission(Long id, Permission permissionDetails) {
		Permission permission = permissionRepository.findById(id)
				.orElseThrow(() -> new ApiException(ApiExceptionCodes.PERMISSION_NOT_FOUND));
		permission.setName(permissionDetails.getName());
		permission.setLabel(permissionDetails.getLabel());
		permission.setParent(permissionDetails.getParent());
		return permissionRepository.save(permission);
	}

	public void deletePermission(Long id) {
		Permission permission = permissionRepository.findById(id)
				.orElseThrow(() -> new ApiException(ApiExceptionCodes.PERMISSION_NOT_FOUND));
		permissionRepository.delete(permission);
	}
}
