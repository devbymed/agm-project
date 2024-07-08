package ma.cimr.agmbackend.mapper;

import static org.mapstruct.ReportingPolicy.IGNORE;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import ma.cimr.agmbackend.dto.request.ActionEditRequest;
import ma.cimr.agmbackend.dto.response.ActionResponse;
import ma.cimr.agmbackend.model.Action;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface ActionMapper {

	ActionResponse toResponse(Action action);

	List<ActionResponse> toResponseList(List<Action> actions);

	@Mapping(target = "id", ignore = true)
	Action toEntity(ActionEditRequest request);
}
