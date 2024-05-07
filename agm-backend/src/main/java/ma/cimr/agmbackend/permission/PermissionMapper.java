package ma.cimr.agmbackend.permission;

import static org.mapstruct.ReportingPolicy.IGNORE;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface PermissionMapper {
	PermissionResponse toPermissionResponse(Permission permission);
}
