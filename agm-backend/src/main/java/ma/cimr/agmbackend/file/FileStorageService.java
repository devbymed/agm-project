package ma.cimr.agmbackend.file;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

	String saveFile(MultipartFile sourceFile, String fileUploadSubPath);
}
