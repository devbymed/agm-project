package ma.cimr.agmbackend.assembly;

import static org.mapstruct.ReportingPolicy.IGNORE;

import org.mapstruct.Mapper;

import ma.cimr.agmbackend.action.ActionMapper;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE, uses = { ActionMapper.class })
public interface AssemblyMapper {

	Assembly toEntity(AssemblyCreateRequest assemblyCreateRequest);

	AssemblyResponse toResponse(Assembly assembly);
}
