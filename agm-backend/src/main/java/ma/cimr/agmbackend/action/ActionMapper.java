package ma.cimr.agmbackend.action;

import static org.mapstruct.ReportingPolicy.IGNORE;

import java.util.List;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface ActionMapper {

	ActionResponse toResponse(Action action);

	List<ActionResponse> toResponseList(List<Action> actions);
}
