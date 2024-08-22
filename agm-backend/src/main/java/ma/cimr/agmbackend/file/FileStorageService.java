package ma.cimr.agmbackend.file;

import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

	void init();

	String save(MultipartFile file, String subDir); // Modification ici

	Resource load(String filename);

	void deleteFile(String filePath);

	void deleteAll();

	Stream<Path> loadAll();
}
