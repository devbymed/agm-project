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
		if (routeSheet != null) {
			fileStorageService.save(routeSheet, "routeSheets");
			assembly.setRouteSheet("routeSheets/" + routeSheet.getOriginalFilename());

			// Lire les actions depuis la feuille de route
			try {
				List<Action> actions = excelService.readActionsFromExcel(routeSheet);
				actions.forEach(action -> action.setAssembly(assembly));
				assembly.setActions(actions);
			} catch (IOException e) {
				throw new ApiException(ApiExceptionCodes.DOCUMENT_UPLOAD_FAILED, e);
			}
		}

		if (invitationLetter != null) {
			String invitationLetterFilename = fileStorageService.save(invitationLetter, "invitationLetters");
			assembly.setInvitationLetter("invitationLetters/" + invitationLetterFilename);
		}
		if (attendanceSheet != null) {
			String attendanceSheetFilename = fileStorageService.save(attendanceSheet, "attendanceSheets");
			assembly.setAttendanceSheet("attendanceSheets/" + attendanceSheetFilename);
		}
		if (proxy != null) {
			String proxyFilename = fileStorageService.save(proxy, "proxies");
			assembly.setProxy("proxies/" + proxyFilename);
		}
		if (attendanceForm != null) {
			fileStorageService.save(attendanceForm, "attendanceForms");
			assembly.setAttendanceForm("attendanceForms/" + attendanceForm.getOriginalFilename());
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
			MultipartFile invitationLetter, MultipartFile attendanceSheet,
			MultipartFile proxy, MultipartFile attendanceForm) {
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
			fileStorageService.save(routeSheet, "routeSheets");
			assembly.setRouteSheet("routeSheets/" + routeSheet.getOriginalFilename());

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
			fileStorageService.save(invitationLetter, "invitationLetters");
			assembly.setInvitationLetter("invitationLetters/" + invitationLetter.getOriginalFilename());
		}
		if (attendanceSheet != null) {
			fileStorageService.save(attendanceSheet, "attendanceSheets");
			assembly.setAttendanceSheet("attendanceSheets/" + attendanceSheet.getOriginalFilename());
		}
		if (proxy != null) {
			fileStorageService.save(proxy, "proxies");
			assembly.setProxy("proxies/" + proxy.getOriginalFilename());
		}
		if (attendanceForm != null) {
			fileStorageService.save(attendanceForm, "attendanceForms");
			assembly.setAttendanceForm("attendanceForms/" + attendanceForm.getOriginalFilename());
		}

		Assembly updatedAssembly = assemblyRepository.save(assembly);
		return assemblyMapper.toResponse(updatedAssembly);
	}

	@Override
	public void deleteCurrentAssembly() {
		Assembly currentAssembly = assemblyRepository.findByClosed(false)
				.orElseThrow(() -> new ApiException(ApiExceptionCodes.ASSEMBLY_NOT_FOUND));
		// Supprimer les fichiers associés
		if (currentAssembly.getRouteSheet() != null) {
			fileStorageService.deleteFile(currentAssembly.getRouteSheet());
		}
		if (currentAssembly.getInvitationLetter() != null) {
			fileStorageService.deleteFile(currentAssembly.getInvitationLetter());
		}
		if (currentAssembly.getAttendanceSheet() != null) {
			fileStorageService.deleteFile(currentAssembly.getAttendanceSheet());
		}
		if (currentAssembly.getProxy() != null) {
			fileStorageService.deleteFile(currentAssembly.getProxy());
		}
		if (currentAssembly.getAttendanceForm() != null) {
			fileStorageService.deleteFile(currentAssembly.getAttendanceForm());
		}
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
}
