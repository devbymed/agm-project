package ma.cimr.agmbackend.assembly;

import static org.mapstruct.ReportingPolicy.IGNORE;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import ma.cimr.agmbackend.action.Action;
import ma.cimr.agmbackend.action.ActionResponse;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface AssemblyMapper {

	@Mapping(target = "actions", ignore = true)
	Assembly toEntity(AssemblyCreateRequest dto);

	AssemblyResponse toResponse(Assembly entity);

	ActionResponse toResponse(Action entity);
}
