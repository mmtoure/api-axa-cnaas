package sn.axa.apiaxacnaas.services;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.Normalizer;
import java.util.Objects;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path root = Paths.get("documents_claim");

    public String store(MultipartFile file, String subFolder) throws IOException {
        Files.createDirectories(root.resolve(subFolder));

        //String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        String cleanFileName = StringUtils.cleanPath(
                Objects.requireNonNull(file.getOriginalFilename())
        );
        String safeName = Normalizer
                .normalize(cleanFileName, Normalizer.Form.NFD)
                .replaceAll("[^\\w.-]", "_");
        String fileName = UUID.randomUUID() + "_" + safeName;

        Path destination = root
                .resolve(subFolder)
                .resolve(fileName)
                .normalize();

        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

        return destination.toAbsolutePath().toString();
    }

    public Resource load(String path) throws IOException {
        Path file = Paths.get(path);
        Resource resource = new UrlResource(file.toUri());

        if (!resource.exists()) {
            throw new RuntimeException("Fichier introuvable");
        }
        return resource;
    }
}
