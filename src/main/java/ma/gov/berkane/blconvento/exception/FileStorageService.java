package ma.gov.berkane.blconvento.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path storageLocation;

    public FileStorageService(
            @Value("${app.storage.location:uploads/conventions}") String location) {

        this.storageLocation = Paths.get(location)
                .toAbsolutePath()
                .normalize();

        try {
            Files.createDirectories(storageLocation);
        } catch (IOException e) {
            throw new RuntimeException(
                    "Impossible de créer le dossier de stockage", e
            );
        }
    }

    public String store(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            return null;
        }

        String originalName = file.getOriginalFilename();

        if (originalName == null ||
                !originalName.toLowerCase().endsWith(".pdf")) {
            throw new IllegalArgumentException(
                    "Seuls les fichiers PDF sont acceptés."
            );
        }

        String fileName =
                UUID.randomUUID() + ".pdf";

        Path destination =
                storageLocation.resolve(fileName);

        try {

            Files.copy(
                    file.getInputStream(),
                    destination,
                    StandardCopyOption.REPLACE_EXISTING
            );

            return storageLocation
                    .relativize(destination)
                    .toString()
                    .replace("\\", "/");

        } catch (IOException e) {

            throw new RuntimeException(
                    "Erreur lors de l'enregistrement du fichier", e
            );
        }
    }

    public void delete(String path) {

        if (path == null || path.isBlank()) {
            return;
        }

        try {
            Path file = Paths.get(path);

            Files.deleteIfExists(file);

        } catch (IOException e) {

            throw new RuntimeException(
                    "Erreur lors de la suppression du fichier", e
            );
        }
    }
}