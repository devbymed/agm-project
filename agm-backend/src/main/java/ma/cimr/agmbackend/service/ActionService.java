package ma.cimr.agmbackend.service;

import java.util.List;

import ma.cimr.agmbackend.dto.request.ActionEditRequest;
import ma.cimr.agmbackend.dto.response.ActionResponse;

public interface ActionService {

	List<ActionResponse> getAllActions();

	ActionResponse getActionById(Long id);

	ActionResponse closeAction(Long id);

	ActionResponse updateAction(Long id, ActionEditRequest request);
}
