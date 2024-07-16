package ma.cimr.agmbackend.assembly;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import ma.cimr.agmbackend.action.Action;
import ma.cimr.agmbackend.exception.ApiException;
import ma.cimr.agmbackend.exception.ApiExceptionCodes;
import ma.cimr.agmbackend.file.FileStorageService;

@Service
@RequiredArgsConstructor
public class AssemblyServiceImpl implements AssemblyService {

	private final AssemblyRepository assemblyRepository;
	private final FileStorageService fileStorageService;
	private final AssemblyMapper assemblyMapper;
	private final AssemblyExcelService excelService;

	@Override
	public AssemblyResponse createAssembly(AssemblyCreateRequest request, MultipartFile routeSheet,
			MultipartFile invitationLetter, MultipartFile attendanceSheet,
			MultipartFile proxy, MultipartFile attendanceForm) {
		Assembly assembly = assemblyMapper.toEntity(request);

		// Gérer l'upload des fichiers et définir les chemins
		String routeSheetPath = normalizePath(fileStorageService.saveFile(routeSheet, "routeSheets"));
		String invitationLetterPath = normalizePath(fileStorageService.saveFile(invitationLetter, "invitationLetters"));
		String attendanceSheetPath = normalizePath(fileStorageService.saveFile(attendanceSheet, "attendanceSheets"));
		String proxyPath = normalizePath(fileStorageService.saveFile(proxy, "proxies"));
		String attendanceFormPath = normalizePath(fileStorageService.saveFile(attendanceForm, "attendanceForms"));

		// Set paths in assembly
		assembly.setRouteSheet(routeSheetPath);
		assembly.setInvitationLetter(invitationLetterPath);
		assembly.setAttendanceSheet(attendanceSheetPath);
		assembly.setProxy(proxyPath);
		assembly.setAttendanceForm(attendanceFormPath);

		// Read actions from route sheet
		if (routeSheet != null) {
			try {
				List<Action> actions = excelService.readActionsFromExcel(routeSheet);
				actions.forEach(action -> action.setAssembly(assembly));
				assembly.setActions(actions);
			} catch (IOException e) {
				throw new ApiException(ApiExceptionCodes.DOCUMENT_UPLOAD_FAILED, e);
			}
		}

		Assembly savedAssembly = assemblyRepository.save(assembly);
		return assemblyMapper.toResponse(savedAssembly);
	}

	@Override
	public AssemblyResponse getCurrentAssembly() {
		Assembly currentAssembly = assemblyRepository.findByClosed(false)
				.orElseThrow(() -> new ApiException(ApiExceptionCodes.ASSEMBLY_NOT_FOUND));
		return assemblyMapper.toResponse(currentAssembly);
	}

	@Override
	public AssemblyResponse updateCurrentAssembly(AssemblyEditRequest request, MultipartFile routeSheet,
			MultipartFile invitationLetter, MultipartFile attendanceSheet, MultipartFile proxy,
			MultipartFile attendanceForm) {
		Assembly assembly = assemblyRepository.findByClosed(false)
				.orElseThrow(() -> new ApiException(ApiExceptionCodes.ASSEMBLY_NOT_FOUND));

		// Mettre à jour les champs de l'assemblée depuis la requête
		if (request.getType() != null) {
			assembly.setType(request.getType());
		}
		if (request.getYear() != null) {
			assembly.setYear(request.getYear());
		}
		if (request.getDay() != null) {
			assembly.setDay(request.getDay());
		}
		if (request.getTime() != null) {
			assembly.setTime(request.getTime());
		}
		if (request.getAddress() != null) {
			assembly.setAddress(request.getAddress());
		}
		if (request.getCity() != null) {
			assembly.setCity(request.getCity());
		}

		// Gérer les fichiers et documents
		if (routeSheet != null) {
			String routeSheetPath = normalizePath(fileStorageService.saveFile(routeSheet, "routeSheets"));
			assembly.setRouteSheet(routeSheetPath);

			// Réextraire les actions depuis le nouveau fichier Excel
			try {
				List<Action> actions = excelService.readActionsFromExcel(routeSheet);
				actions.forEach(action -> action.setAssembly(assembly));
				assembly.getActions().clear(); // Supprimer les anciennes actions
				assembly.setActions(actions);
			} catch (IOException e) {
				throw new ApiException(ApiExceptionCodes.DOCUMENT_UPLOAD_FAILED, e);
			}
		}
		if (invitationLetter != null) {
			String invitationLetterPath = normalizePath(fileStorageService.saveFile(invitationLetter, "invitationLetters"));
			assembly.setInvitationLetter(invitationLetterPath);
		}
		if (attendanceSheet != null) {
			String attendanceSheetPath = normalizePath(fileStorageService.saveFile(attendanceSheet, "attendanceSheets"));
			assembly.setAttendanceSheet(attendanceSheetPath);
		}
		if (proxy != null) {
			String proxyPath = normalizePath(fileStorageService.saveFile(proxy, "proxies"));
			assembly.setProxy(proxyPath);
		}
		if (attendanceForm != null) {
			String attendanceFormPath = normalizePath(fileStorageService.saveFile(attendanceForm, "attendanceForms"));
			assembly.setAttendanceForm(attendanceFormPath);
		}

		Assembly updatedAssembly = assemblyRepository.save(assembly);
		return assemblyMapper.toResponse(updatedAssembly);
	}

	@Override
	public void deleteCurrentAssembly() {
		Assembly currentAssembly = assemblyRepository.findByClosed(false)
				.orElseThrow(() -> new ApiException(ApiExceptionCodes.ASSEMBLY_NOT_FOUND));
		assemblyRepository.delete(currentAssembly);
	}

	@Override
	public AssemblyResponse closeCurrentAssembly() {
		Assembly currentAssembly = assemblyRepository.findByClosed(false)
				.orElseThrow(() -> new ApiException(ApiExceptionCodes.ASSEMBLY_NOT_FOUND));
		if (currentAssembly.isClosed()) {
			throw new ApiException(ApiExceptionCodes.ASSEMBLY_ALREADY_CLOSED);
		}

		currentAssembly.getActions().clear();
		currentAssembly.setClosed(true);
		Assembly closedAssembly = assemblyRepository.save(currentAssembly);
		return assemblyMapper.toResponse(closedAssembly);
	}

	@Override
	public boolean hasCurrentAssembly() {
		return assemblyRepository.existsByClosed(false);
	}

	private String normalizePath(String path) {
		return path.replace("\\", "/");
	}
}
