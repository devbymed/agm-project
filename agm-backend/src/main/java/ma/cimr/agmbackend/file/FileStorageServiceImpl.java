package ma.cimr.agmbackend.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageServiceImpl implements FileStorageService {

	private final Path root = Paths.get("uploads");

	@Override
	public void init() {
		try {
			Files.createDirectories(root);
		} catch (IOException e) {
			throw new RuntimeException("Could not initialize folder for upload!", e);
		}
	}

	@Override
	public void save(MultipartFile file, String fileName) {
		try {
			Path targetLocation = this.root.resolve(fileName);
			int counter = 1;
			while (Files.exists(targetLocation)) {
				String newFileName = getNewFileName(fileName, counter);
				targetLocation = this.root.resolve(newFileName);
				counter++;
			}
			Files.copy(file.getInputStream(), targetLocation);
		} catch (Exception e) {
			throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
		}
	}

	private String getNewFileName(String fileName, int counter) {
		String name = fileName.contains(".") ? fileName.substring(0, fileName.lastIndexOf('.')) : fileName;
		String extension = fileName.contains(".") ? fileName.substring(fileName.lastIndexOf('.')) : "";
		return name + "_" + counter + extension;
	}

	@Override
	public Resource load(String filename) {
		try {
			Path file = root.resolve(filename);
			Resource resource = new UrlResource(file.toUri());

			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new RuntimeException("Could not read the file!");
			}
		} catch (Exception e) {
			throw new RuntimeException("Error: " + e.getMessage());
		}
	}

	@Override
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(root.toFile());
	}

	@Override
	public Stream<Path> loadAll() {
		try {
			return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
		} catch (IOException e) {
			throw new RuntimeException("Could not load the files!", e);
		}
	}
}
