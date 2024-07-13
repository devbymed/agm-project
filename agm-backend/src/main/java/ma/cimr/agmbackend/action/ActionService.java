package ma.cimr.agmbackend.action;

import java.util.List;

public interface ActionService {

	List<ActionResponse> getAllActions();

	ActionResponse getActionById(Long id);

	ActionResponse closeAction(Long id);

	ActionResponse updateAction(Long id, ActionEditRequest request);
}
