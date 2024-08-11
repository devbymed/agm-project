package ma.cimr.agmbackend.action;

import static org.mapstruct.ReportingPolicy.IGNORE;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface ActionMapper {

	@Mapping(source = "attachments", target = "attachments")
	ActionResponse toResponse(Action action);

	List<ActionResponse> toResponseList(List<Action> actions);

	ActionAttachmentResponse toAttachmentResponse(ActionAttachment attachment);

	List<ActionAttachmentResponse> toAttachmentResponseList(List<ActionAttachment> attachments);
}
