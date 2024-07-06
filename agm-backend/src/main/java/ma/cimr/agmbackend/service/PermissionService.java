package ma.cimr.agmbackend.service;

import java.util.List;

import ma.cimr.agmbackend.dto.response.PermissionResponse;

public interface PermissionService {

	List<PermissionResponse> getPermissionsHierarchy();
}
