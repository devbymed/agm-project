package ma.cimr.agmbackend.service.impl;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import ma.cimr.agmbackend.dto.request.AssemblyCreateRequest;
import ma.cimr.agmbackend.dto.response.AssemblyResponse;
import ma.cimr.agmbackend.exception.ApiException;
import ma.cimr.agmbackend.exception.ApiExceptionCodes;
import ma.cimr.agmbackend.mapper.AssemblyMapper;
import ma.cimr.agmbackend.model.Action;
import ma.cimr.agmbackend.model.Assembly;
import ma.cimr.agmbackend.model.Document;
import ma.cimr.agmbackend.repository.AssemblyRepository;
import ma.cimr.agmbackend.service.AssemblyService;
import ma.cimr.agmbackend.service.ExcelService;
import ma.cimr.agmbackend.service.FileStorageService;

@Service
@RequiredArgsConstructor
public class AssemblyServiceImpl implements AssemblyService {

	private static final Logger logger = LoggerFactory.getLogger(AssemblyServiceImpl.class);
	private final AssemblyRepository assemblyRepository;
	private final FileStorageService fileStorageService;
	private final AssemblyMapper assemblyMapper;
	private final ExcelService excelService;

	@Override
	public AssemblyResponse createAssembly(AssemblyCreateRequest request, MultipartFile routeSheet,
			MultipartFile invitationLetter, MultipartFile attendanceSheet, MultipartFile proxy,
			MultipartFile attendanceForm) {
		Assembly assembly = assemblyMapper.toEntity(request);

		handleFilesAndDocuments(assembly, routeSheet, invitationLetter, attendanceSheet, proxy, attendanceForm);

		// Lire les actions depuis le fichier Excel
		if (routeSheet != null) {
			try {
				List<Action> actions = excelService.readActionsFromExcel(routeSheet);
				actions.forEach(action -> action.setAssembly(assembly));
				assembly.setActions(actions);
				logger.info("Actions to be saved: " + actions.size());
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
	public AssemblyResponse updateAssembly(Long id, AssemblyCreateRequest request, MultipartFile routeSheet,
			MultipartFile invitationLetter, MultipartFile attendanceSheet, MultipartFile proxy,
			MultipartFile attendanceForm) {
		Assembly assembly = assemblyRepository.findById(id)
				.orElseThrow(() -> new ApiException(ApiExceptionCodes.ASSEMBLY_NOT_FOUND));

		// Update assembly fields from request
		assembly.setType(request.getType());
		assembly.setYear(request.getYear());
		assembly.setDay(request.getDay());
		assembly.setTime(request.getTime());
		assembly.setAddress(request.getAddress());
		assembly.setCity(request.getCity());

		try {
			handleFilesAndDocuments(assembly, routeSheet, invitationLetter, attendanceSheet, proxy, attendanceForm);
		} catch (Exception e) {
			throw new ApiException(ApiExceptionCodes.DOCUMENT_UPLOAD_FAILED, e);
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

	private void handleFilesAndDocuments(Assembly assembly, MultipartFile routeSheet, MultipartFile invitationLetter,
			MultipartFile attendanceSheet, MultipartFile proxy, MultipartFile attendanceForm) {
		if (routeSheet != null) {
			fileStorageService.save(routeSheet, routeSheet.getOriginalFilename());
			Document routeSheetDoc = new Document(routeSheet.getOriginalFilename(),
					"uploads/" + routeSheet.getOriginalFilename());
			assembly.setRouteSheet(routeSheetDoc);
		}

		if (invitationLetter != null) {
			fileStorageService.save(invitationLetter, invitationLetter.getOriginalFilename());
			Document invitationLetterDoc = new Document(invitationLetter.getOriginalFilename(),
					"uploads/" + invitationLetter.getOriginalFilename());
			assembly.setInvitationLetter(invitationLetterDoc);
		}

		if (attendanceSheet != null) {
			fileStorageService.save(attendanceSheet, attendanceSheet.getOriginalFilename());
			Document attendanceSheetDoc = new Document(attendanceSheet.getOriginalFilename(),
					"uploads/" + attendanceSheet.getOriginalFilename());
			assembly.setAttendanceSheet(attendanceSheetDoc);
		}

		if (proxy != null) {
			fileStorageService.save(proxy, proxy.getOriginalFilename());
			Document proxyDoc = new Document(proxy.getOriginalFilename(), "uploads/" + proxy.getOriginalFilename());
			assembly.setProxy(proxyDoc);
		}

		if (attendanceForm != null) {
			fileStorageService.save(attendanceForm, attendanceForm.getOriginalFilename());
			Document attendanceFormDoc = new Document(attendanceForm.getOriginalFilename(),
					"uploads/" + attendanceForm.getOriginalFilename());
			assembly.setAttendanceForm(attendanceFormDoc);
		}
	}
}
