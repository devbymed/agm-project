package ma.cimr.agmbackend.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ma.cimr.agmbackend.dto.request.ActionEditRequest;
import ma.cimr.agmbackend.dto.response.ActionResponse;
import ma.cimr.agmbackend.exception.ApiException;
import ma.cimr.agmbackend.exception.ApiExceptionCodes;
import ma.cimr.agmbackend.mapper.ActionMapper;
import ma.cimr.agmbackend.model.Action;
import ma.cimr.agmbackend.repository.ActionRepository;
import ma.cimr.agmbackend.service.ActionService;

@Service
@RequiredArgsConstructor
public class ActionServiceImpl implements ActionService {

	private final ActionRepository actionRepository;
	private final ActionMapper actionMapper;

	@Override
	public List<ActionResponse> getAllActions() {
		List<Action> actions = actionRepository.findAll();
		return actionMapper.toResponseList(actions);
	}

	@Override
	public ActionResponse getActionById(Long id) {
		Action action = actionRepository.findById(id)
				.orElseThrow(() -> new ApiException(ApiExceptionCodes.ACTION_NOT_FOUND));
		return actionMapper.toResponse(action);
	}

	@Override
	public ActionResponse closeAction(Long id) {
		Action action = actionRepository.findById(id)
				.orElseThrow(() -> new ApiException(ApiExceptionCodes.ACTION_NOT_FOUND));
		// action.setProgressStatus("Closed");
		// action.setRealizationDate(LocalDate.now());
		Action closedAction = actionRepository.save(action);
		return actionMapper.toResponse(closedAction);
	}

	@Override
	public ActionResponse updateAction(Long id, ActionEditRequest request) {
		Action action = actionRepository.findById(id)
				.orElseThrow(() -> new ApiException(ApiExceptionCodes.ACTION_NOT_FOUND));

		if (request.getDescription() != null) {
			action.setDescription(request.getDescription());
		}
		if (request.getResponsible() != null) {
			action.setResponsible(request.getResponsible());
		}
		if (request.getStartDate() != null) {
			action.setStartDate(request.getStartDate());
		}
		if (request.getEndDate() != null) {
			action.setEndDate(request.getEndDate());
		}
		if (request.getProgressStatus() != null) {
			action.setProgressStatus(request.getProgressStatus());
		}
		if (request.getRealizationDate() != null) {
			action.setRealizationDate(request.getRealizationDate());
		}
		if (request.getObservation() != null) {
			action.setObservation(request.getObservation());
		}
		if (request.getDeliverable() != null) {
			action.setDeliverable(request.getDeliverable());
		}

		Action updatedAction = actionRepository.save(action);
		return actionMapper.toResponse(updatedAction);
	}
}
