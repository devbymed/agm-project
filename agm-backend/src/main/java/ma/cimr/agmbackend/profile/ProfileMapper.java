package ma.cimr.agmbackend.profile;

import static org.mapstruct.ReportingPolicy.IGNORE;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import ma.cimr.agmbackend.permission.Permission;
import ma.cimr.agmbackend.permission.PermissionMapper;
import ma.cimr.agmbackend.permission.PermissionResponse;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface ProfileMapper {
	@Named("toProfileResponse")
	@Mapping(target = "permissions", qualifiedByName = "mapPermissionsToResponses")
	ProfileResponse toProfileResponse(Profile profile);

	Profile toProfile(ProfileAddRequest profileAddRequest);

	@Named("mapPermissionsToResponses")
	default List<PermissionResponse> mapPermissionsToResponses(Set<Permission> permissions) {
		if (permissions == null) {
			return null;
		}
		return permissions.stream()
				.map(permission -> PermissionMapper.INSTANCE.toPermissionResponse(permission))
				.collect(Collectors.toList());
	}

	@Named("toProfileResponseWithoutPermissions")
	@Mapping(target = "permissions", ignore = true)
	ProfileResponse toProfileResponseWithoutPermissions(Profile profile);
}
