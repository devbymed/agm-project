package ma.cimr.agmbackend.permission;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

	private final PermissionRepository permissionRepository;
	private final PermissionMapper permissionMapper;

	public List<PermissionResponse> getAllPermissions() {
		return permissionRepository.findAll().stream()
				.map(permissionMapper::toPermissionResponse)
				.collect(Collectors.toList());
	}
}
