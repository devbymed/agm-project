package ma.cimr.agmbackend.file;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FileStorageServiceImpl implements FileStorageService {

	private final Path root;

	public FileStorageServiceImpl(@Value("${file.uploads.output-path}") String uploadDir) {
		this.root = Paths.get(uploadDir).toAbsolutePath().normalize();
		init(); // Initialize the folder when the service is created
	}

	@Override
	public void init() {
		try {
			Files.createDirectories(root);
		} catch (IOException e) {
			throw new RuntimeException("Could not initialize folder for upload!", e);
		}
	}

	@Override
	public String save(MultipartFile file, String subDir) {
		try {
			// Générer un nom de fichier unique
			String uniqueFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
			log.info("Saving file with name: " + uniqueFileName);

			// Construire le chemin cible avec le sous-répertoire
			Path targetLocation = this.root.resolve(Paths.get(subDir)).normalize().resolve(uniqueFileName);
			Files.createDirectories(targetLocation.getParent()); // Crée le sous-répertoire s'il n'existe pas

			// Sauvegarder le fichier
			Files.copy(file.getInputStream(), targetLocation);

			// Retourner le nom de fichier complet (avec UUID)
			return uniqueFileName;
		} catch (FileAlreadyExistsException e) {
			throw new RuntimeException("A file with that name already exists.", e);
		} catch (IOException e) {
			throw new RuntimeException("Could not store the file. Error: " + e.getMessage(), e);
		}
	}

	@Override
	public Resource load(String filename) {
		try {
			Path file = root.resolve(filename).normalize();
			log.info("Attempting to load file from path: " + file.toString());

			Resource resource = new UrlResource(file.toUri());

			if (resource.exists() && resource.isReadable()) {
				return resource;
			} else {
				throw new RuntimeException("Could not read the file at path: " + file.toString());
			}
		} catch (MalformedURLException e) {
			throw new RuntimeException("Error: " + e.getMessage(), e);
		}
	}

	@Override
	public void deleteFile(String filePath) {
		try {
			Path path = this.root.resolve(filePath).normalize();
			Files.deleteIfExists(path);
		} catch (IOException e) {
			throw new RuntimeException("Could not delete the file. Error: " + e.getMessage(), e);
		}
	}

	@Override
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(root.toFile());
	}

	@Override
	public Stream<Path> loadAll() {
		try {
			return Files.walk(this.root, 1)
					.filter(path -> !path.equals(this.root))
					.map(this.root::relativize);
		} catch (IOException e) {
			throw new RuntimeException("Could not load the files!", e);
		}
	}
}
