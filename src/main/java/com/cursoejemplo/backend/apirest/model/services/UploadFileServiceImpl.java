package com.cursoejemplo.backend.apirest.model.services;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class UploadFileServiceImpl implements IUploadFileService {

    private final static String DIRECTORIO_UPLOAD = "uploads";

    @Override
    public Resource cargar(String fileName) throws MalformedURLException {
        Path rutFile = getPath(fileName);
        Resource resource = new UrlResource(rutFile.toUri());

        if (!resource.exists() && !resource.isReadable()) {
            rutFile = Paths.get("src/main/resources/static/images").resolve("no-usuario.png").toAbsolutePath();
            resource = new UrlResource(rutFile.toUri());
        }

        return resource;
    }

    @Override
    public String copiarFile(MultipartFile file) throws IOException {
        String nameFile = UUID.randomUUID().toString() + "_" + file.getOriginalFilename().replace(" ", "");
        Path rutFile = getPath(nameFile);
        Files.copy(file.getInputStream(), rutFile);

        return nameFile;
    }

    @Override
    public boolean eliminarFile(String nameFile) {
        if (nameFile != null && nameFile.length() > 0) {
            Path rutOld = Paths.get("uploads").resolve(nameFile).toAbsolutePath();
            File fileOld = rutOld.toFile();
            if (fileOld.exists() && fileOld.canRead()) {
                fileOld.delete();
                return true;
            }
        }
        return false;
    }

    @Override
    public Path getPath(String nameFile) {
        return Paths.get(DIRECTORIO_UPLOAD).resolve(nameFile).toAbsolutePath();
    }
}
