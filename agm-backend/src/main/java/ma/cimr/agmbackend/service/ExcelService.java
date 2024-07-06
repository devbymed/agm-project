package ma.cimr.agmbackend.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import ma.cimr.agmbackend.model.Action;

public interface ExcelService {

	List<Action> readActionsFromExcel(MultipartFile file) throws IOException;
}
