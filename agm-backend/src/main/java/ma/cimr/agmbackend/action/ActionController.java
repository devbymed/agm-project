package ma.cimr.agmbackend.action;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.cimr.agmbackend.file.FileStorageService;
import ma.cimr.agmbackend.util.ApiResponse;
import ma.cimr.agmbackend.util.ApiResponseFormatter;

@RestController
@RequestMapping("actions")
@RequiredArgsConstructor
public class ActionController {

	private final ActionService actionService;
	private final FileStorageService fileStorageService;

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
			@Valid @ModelAttribute ActionEditRequest request) {
		ActionResponse updatedAction = actionService.updateAction(id, request);
		return ApiResponseFormatter.generateResponse(HttpStatus.OK, "Action mise à jour avec succès", updatedAction);
	}

	@GetMapping("/{actionId}/attachments/{fileName:.+}")
	public ResponseEntity<Resource> downloadAttachment(@PathVariable Long actionId, @PathVariable String fileName) {
		// Récupérer l'attachement correspondant à actionId et fileName
		ActionResponse action = actionService.getActionById(actionId);
		String storedFilePath = action.getAttachments().stream()
				.filter(attachment -> attachment.getFileName().equals(fileName))
				.map(ActionAttachmentResponse::getFilePath)
				.findFirst()
				.orElseThrow(() -> new RuntimeException("File not found"));

		// Charger le fichier depuis le service de stockage
		Resource file = fileStorageService.load(storedFilePath);

		// Extraire le nom original du fichier sans UUID
		String originalFileName = fileName.substring(fileName.indexOf("_") + 1);

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + originalFileName + "\"")
				.body(file);
	}

}
