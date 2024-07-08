package ma.cimr.agmbackend.service;

import org.springframework.web.multipart.MultipartFile;

import ma.cimr.agmbackend.dto.request.AssemblyCreateRequest;
import ma.cimr.agmbackend.dto.response.AssemblyResponse;

public interface AssemblyService {

	AssemblyResponse createAssembly(AssemblyCreateRequest request, MultipartFile routeSheet,
			MultipartFile invitationLetter, MultipartFile attendanceSheet, MultipartFile proxy, MultipartFile attendanceForm);

	AssemblyResponse getCurrentAssembly();

	AssemblyResponse updateAssembly(Long id, AssemblyCreateRequest request, MultipartFile routeSheet,
			MultipartFile invitationLetter, MultipartFile attendanceSheet, MultipartFile proxy, MultipartFile attendanceForm);

	void deleteCurrentAssembly();

	AssemblyResponse closeCurrentAssembly();
}
