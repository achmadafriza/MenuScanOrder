package net.uqcloud.infs7202.project.service.file;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public interface FileStorageService {
    default String saveFile(MultipartFile file) throws IOException {
        String uploadDir = getRootDirectory() + getDirectory();
        String fileName = cleanFileName(file);
        Path path = resolveUploadDir(uploadDir, fileName);

        write(path, file);

        return resolveAbsoluteURL(getDirectory(), fileName);
    }

    default Path resolveUploadDir(String directory, String fileName) throws IOException {
        Path uploadPath = Paths.get(directory);
        if(!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        return uploadPath.resolve(fileName);
    }

    default void write(Path path, MultipartFile file) throws IOException {
        Files.write(path, file.getBytes());
    }

    default String cleanFileName(MultipartFile file) {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        fileName = String.format("%s_%s.%s",
                fileName.substring(0, fileName.lastIndexOf(".")),
                RandomStringUtils.randomAlphanumeric(8),
                fileName.substring(fileName.lastIndexOf(".") + 1));
        return fileName;
    }

    String getDirectory();
    String getRootDirectory();
    String resolveAbsoluteURL(String directory, String filename);
}
