package ma.cimr.agmbackend.action;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import ma.cimr.agmbackend.exception.ApiException;
import ma.cimr.agmbackend.exception.ApiExceptionCodes;
import ma.cimr.agmbackend.file.FileStorageService;

@Service
@RequiredArgsConstructor
public class ActionServiceImpl implements ActionService {

	private final ActionRepository actionRepository;
	private final FileStorageService fileStorageService;
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
	public List<ActionResponse> getOverdueUnclosedActions() {
		LocalDate currentDate = LocalDate.now();
		List<Action> actions = actionRepository.findOverdueUnclosedActions(currentDate);
		return actions.stream()
				.map(actionMapper::toResponse)
				.collect(Collectors.toList());
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

		if (request.getName() != null) {
			action.setName(request.getName());
		}
		if (request.getEntity() != null) {
			action.setEntity(request.getEntity());
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

		if (request.getAttachments() != null && !request.getAttachments().isEmpty()) {
			for (MultipartFile file : request.getAttachments()) {
				String subDir = "actions/" + id;
				String fileName = fileStorageService.save(file, subDir);

				if (fileName != null) {
					ActionAttachment attachment = ActionAttachment.builder()
							.action(action)
							.fileName(fileName) // Stocke le nom de fichier complet avec UUID
							.originalFileName(file.getOriginalFilename()) // Stocke le nom original du fichier
							.fileType(file.getContentType())
							.filePath(subDir + "/" + fileName)
							.fileSize(file.getSize()) // Stocke la taille du fichier
							.build();

					action.getAttachments().add(attachment);
				}
			}
		}

		Action updatedAction = actionRepository.save(action);
		return actionMapper.toResponse(updatedAction);
	}
}
