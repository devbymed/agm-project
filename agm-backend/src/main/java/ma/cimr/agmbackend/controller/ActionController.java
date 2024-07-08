package ma.cimr.agmbackend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.cimr.agmbackend.dto.request.ActionEditRequest;
import ma.cimr.agmbackend.dto.response.ActionResponse;
import ma.cimr.agmbackend.service.ActionService;
import ma.cimr.agmbackend.util.ApiResponse;
import ma.cimr.agmbackend.util.ApiResponseFormatter;

@RestController
@RequestMapping("actions")
@RequiredArgsConstructor
public class ActionController {

	private final ActionService actionService;

	@GetMapping
	public ResponseEntity<ApiResponse> getAllActions() {
		List<ActionResponse> actions = actionService.getAllActions();
		return ApiResponseFormatter.generateResponse(HttpStatus.OK, actions);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse> getActionById(@PathVariable Long id) {
		ActionResponse action = actionService.getActionById(id);
		return ApiResponseFormatter.generateResponse(HttpStatus.OK, action);
	}

	@PatchMapping("/{id}/close")
	public ResponseEntity<ApiResponse> closeAction(@PathVariable Long id) {
		ActionResponse closedAction = actionService.closeAction(id);
		return ApiResponseFormatter.generateResponse(HttpStatus.OK, "Action clôturée avec succès", closedAction);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<ApiResponse> updateAction(@PathVariable Long id,
			@Valid @RequestBody ActionEditRequest request) {
		ActionResponse updatedAction = actionService.updateAction(id, request);
		return ApiResponseFormatter.generateResponse(HttpStatus.OK, "Action mise à jour avec succès", updatedAction);
	}
}
