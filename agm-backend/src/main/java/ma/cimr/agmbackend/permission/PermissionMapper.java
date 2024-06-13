package ma.cimr.agmbackend.permission;

import static org.mapstruct.ReportingPolicy.IGNORE;

import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface PermissionMapper {
	PermissionMapper INSTANCE = Mappers.getMapper(PermissionMapper.class);

	PermissionResponse toPermissionResponse(Permission permission);

	@Named("toPermissionResponseList")
	default List<PermissionResponse> toPermissionResponseList(List<Permission> permissions) {
		if (permissions == null) {
			return null;
		}
		return permissions.stream()
				.map(this::toPermissionResponse)
				.collect(Collectors.toList());
	}
}
