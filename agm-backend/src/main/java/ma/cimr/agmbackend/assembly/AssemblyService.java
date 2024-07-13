package ma.cimr.agmbackend.assembly;

import org.springframework.web.multipart.MultipartFile;

public interface AssemblyService {

	AssemblyResponse createAssembly(AssemblyCreateRequest request, MultipartFile routeSheet,
			MultipartFile invitationLetter, MultipartFile attendanceSheet, MultipartFile proxy, MultipartFile attendanceForm);

	AssemblyResponse getCurrentAssembly();

	AssemblyResponse updateAssembly(Long id, AssemblyCreateRequest request, MultipartFile routeSheet,
			MultipartFile invitationLetter, MultipartFile attendanceSheet, MultipartFile proxy, MultipartFile attendanceForm);

	void deleteCurrentAssembly();

	AssemblyResponse closeCurrentAssembly();

	boolean hasCurrentAssembly();
}
