package ma.cimr.agmbackend.mapper;

import static org.mapstruct.ReportingPolicy.IGNORE;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import ma.cimr.agmbackend.dto.request.AssemblyCreateRequest;
import ma.cimr.agmbackend.dto.response.ActionResponse;
import ma.cimr.agmbackend.dto.response.AssemblyResponse;
import ma.cimr.agmbackend.model.Action;
import ma.cimr.agmbackend.model.Assembly;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface AssemblyMapper {

	@Mapping(target = "actions", ignore = true)
	Assembly toEntity(AssemblyCreateRequest dto);

	AssemblyResponse toResponse(Assembly entity);

	ActionResponse toResponse(Action entity);
}
