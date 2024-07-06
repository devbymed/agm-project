package ma.cimr.agmbackend.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ma.cimr.agmbackend.dto.response.PermissionResponse;
import ma.cimr.agmbackend.mapper.PermissionMapper;
import ma.cimr.agmbackend.model.Permission;
import ma.cimr.agmbackend.repository.PermissionRepository;
import ma.cimr.agmbackend.service.PermissionService;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

	private final PermissionRepository permissionRepository;
	private final PermissionMapper permissionMapper;

	public List<PermissionResponse> getPermissionsHierarchy() {
		List<Permission> permissions = permissionRepository.findAll();
		List<PermissionResponse> responses = permissionMapper.toResponseList(permissions);

		Map<Long, PermissionResponse> responseMap = responses.stream()
				.collect(Collectors.toMap(PermissionResponse::getId, response -> response));

		List<PermissionResponse> rootPermissions = new ArrayList<>();

		for (PermissionResponse response : responseMap.values()) {
			if (response.getParentId() != null) {
				PermissionResponse parent = responseMap.get(response.getParentId());
				if (parent != null) {
					if (parent.getChildren() == null) {
						parent.setChildren(new ArrayList<>());
					}
					parent.getChildren().add(response);
				}
			} else {
				rootPermissions.add(response);
			}
		}
		return rootPermissions;
	}
}
