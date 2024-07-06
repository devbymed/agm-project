package ma.cimr.agmbackend.mapper;

import static org.mapstruct.ReportingPolicy.IGNORE;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import ma.cimr.agmbackend.dto.response.PermissionResponse;
import ma.cimr.agmbackend.model.Permission;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface PermissionMapper {

	PermissionMapper INSTANCE = Mappers.getMapper(PermissionMapper.class);

	@Mapping(source = "parent.id", target = "parentId")
	PermissionResponse toResponse(Permission permission);

	List<PermissionResponse> toResponseList(List<Permission> permissions);
}
