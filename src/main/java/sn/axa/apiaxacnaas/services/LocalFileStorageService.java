package sn.axa.apiaxacnaas.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;


@Service
public class LocalFileStorageService {
    private final Path root = Paths.get("documents_claim");
    public String store(MultipartFile file, String directory){
        try{
            Path dirPath = root.resolve(directory);
            Files.createDirectories(dirPath);
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = dirPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath);
            return filePath.toString();
        }
        catch (IOException e) {
            throw new RuntimeException("Erreur lors du stockage du fichier");
        }

    }


}
