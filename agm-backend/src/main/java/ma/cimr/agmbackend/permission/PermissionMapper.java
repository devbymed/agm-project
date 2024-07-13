package ma.cimr.agmbackend.permission;

import static org.mapstruct.ReportingPolicy.IGNORE;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface PermissionMapper {

	PermissionMapper INSTANCE = Mappers.getMapper(PermissionMapper.class);

	@Mapping(source = "parent.id", target = "parentId")
	PermissionResponse toResponse(Permission permission);

	List<PermissionResponse> toResponseList(List<Permission> permissions);
}
