package ma.cimr.agmbackend.reason;

import static org.mapstruct.ReportingPolicy.IGNORE;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface ReasonMapper {

	ReasonMapper INSTANCE = Mappers.getMapper(ReasonMapper.class);

	ReasonResponse toResponse(Reason reason);

	@Mapping(target = "id", ignore = true)
	Reason toEntity(ReasonAddRequest reasonAddRequest);

	@Mapping(target = "id", ignore = true)
	void updateEntityFromDto(ReasonEditRequest reasonEditRequest, @MappingTarget Reason reason);
}
