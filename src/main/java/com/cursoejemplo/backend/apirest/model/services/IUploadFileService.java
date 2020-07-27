package com.cursoejemplo.backend.apirest.model.services;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;

public interface IUploadFileService {

    Resource cargar(String fileName) throws MalformedURLException;

    String copiarFile(MultipartFile file) throws IOException;

    boolean eliminarFile(String nameFile);

    Path getPath(String nameFile);

}
