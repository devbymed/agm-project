package ma.cimr.agmbackend.assembly;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import ma.cimr.agmbackend.action.Action;

public interface AssemblyExcelService {

	List<Action> readActionsFromExcel(MultipartFile file) throws IOException;
}
