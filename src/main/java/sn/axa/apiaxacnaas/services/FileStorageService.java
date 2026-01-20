package sn.axa.apiaxacnaas.services;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    String store(MultipartFile file, String directory);
    void deleteFile(String filePath);
}
