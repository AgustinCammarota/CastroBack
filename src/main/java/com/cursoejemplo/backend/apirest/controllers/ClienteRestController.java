package com.cursoejemplo.backend.apirest.controllers;

import com.cursoejemplo.backend.apirest.model.entity.Cliente;
import com.cursoejemplo.backend.apirest.model.entity.Region;
import com.cursoejemplo.backend.apirest.model.services.IClienteService;
import com.cursoejemplo.backend.apirest.model.services.IUploadFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = {"http://localhost:4200", "*"})
@RestController
@RequestMapping("/api")
public class ClienteRestController {

    @Autowired
    private IClienteService clienteService;

    @Autowired
    private IUploadFileService uploadFileService;

    private Cliente clienteActual = null;

    private Map<String, Object> map = new HashMap<>();

    private Logger logger = LoggerFactory.getLogger(ClienteRestController.class);


    @GetMapping("/clientes/page/{page}")
    public Page<Cliente> index(@PathVariable Integer page) {
        return clienteService.findAll(PageRequest.of(page, 4));
    }

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @GetMapping("/clientes/{id}")
    public ResponseEntity<?> show(@PathVariable Long id) {
        try{
            clienteActual = clienteService.findById(id);
        } catch (DataAccessException e) {
            map.put("mensaje", "Error al realizar la consulta en la base de datos");
            map.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(map, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (clienteActual == null) {
            map.put("mensaje", "El cliente ID: ".concat(id.toString().concat(" no existe en la base de datos!")));
            return new ResponseEntity<Map<String, Object>>(map, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Cliente>(clienteActual, HttpStatus.OK);
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/clientes") //Ya que el cliente viene desde el front en formato json, la anotacion @ResquestBody
    //Le permite a spring convertir el json y poblar el objeto.
    public ResponseEntity<?> create(@Valid @RequestBody Cliente cliente, BindingResult result) {

        if(result.hasErrors()) {

            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
                    .collect(Collectors.toList());

            map.put("errors", errors);
            return new ResponseEntity<Map<String, Object>>(map, HttpStatus.BAD_REQUEST);
        }

        try {
            clienteActual = clienteService.save(cliente);
        } catch (DataAccessException e){
            map.put("mensaje", "Error al realizar el insert en la base de datos");
            map.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(map, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        map.put("mensaje", "El cliente ha sido creado con exito!");
        map.put("cliente", clienteActual);
        return new ResponseEntity<Map<String, Object>>(map, HttpStatus.CREATED);
    }

    @Secured("ROLE_ADMIN")
    @PutMapping("/clientes/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody Cliente cliente, BindingResult result ,@PathVariable Long id) {

        clienteActual = clienteService.findById(id);

        if(result.hasErrors()) {

            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
                    .collect(Collectors.toList());

            map.put("errors", errors);
            return new ResponseEntity<Map<String, Object>>(map, HttpStatus.BAD_REQUEST);
        }

        if (clienteActual == null) {
            map.put("mensaje", "Error: no se pudo editar, el cliente ID: "
                    .concat(id.toString().concat(" no existe en la base de datos!")));
            return new ResponseEntity<Map<String, Object>>(map, HttpStatus.NOT_FOUND);
        }
        try {
            clienteActual.setNombre(cliente.getNombre());
            clienteActual.setApellido(cliente.getApellido());
            clienteActual.setEmail(cliente.getEmail());
            clienteActual.setCreateAt(cliente.getCreateAt());
            clienteActual.setRegion(cliente.getRegion());
            clienteService.save(clienteActual);
        }catch (DataAccessException e) {
            map.put("mensaje", "Error al actualizar el cliente en la base de datos");
            map.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(map, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        map.put("mensaje", "El cliente ha sido actualizado con éxito!");
        map.put("cliente", clienteActual);

        return new ResponseEntity<Map<String, Object>>(map, HttpStatus.CREATED);
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping("/clientes/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            clienteActual = clienteService.findById(id);
            String nameFileOld = clienteActual.getFoto();
            uploadFileService.eliminarFile(nameFileOld);
            clienteService.delete(id);
        }catch (DataAccessException e) {
            map.put("mensaje", "Error al eliminar el cliente de la base de datos");
            map.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));

            return new ResponseEntity<Map<String, Object>>(map, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        map.put("mensaje", "El cliente ha sido eliminado con exito!");

        return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @PostMapping("/clientes/upload")
    public ResponseEntity<?> upload(@RequestParam("archivo") MultipartFile archivo, @RequestParam("id") Long id) {
        clienteActual = clienteService.findById(id);

        if(!archivo.isEmpty()) {
            String fileName = null;
            try {
                fileName = uploadFileService.copiarFile(archivo);
            } catch (IOException e) {
                map.put("mensaje", "Error al subir la imagen del cliente ");
                map.put("error", e.getMessage().concat(": ").concat(e.getCause().getMessage()));
               return new ResponseEntity<Map<String, Object>>(map, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            String nameFileOld = clienteActual.getFoto();
            //Si borramos una foto que se borre del directorio uploads
            uploadFileService.eliminarFile(nameFileOld);
            clienteActual.setFoto(fileName);
            clienteService.save(clienteActual);

            map.put("cliente", clienteActual);
            map.put("mensaje", "Ha subido correctamente la imagen: " + fileName);
        }
        return new ResponseEntity<Map<String, Object>>(map, HttpStatus.CREATED);
    }

    @GetMapping("/uploads/img/{nameFoto:.+}")
    public ResponseEntity<Resource> verFoto(@PathVariable String nameFoto) {
        Resource resource = null;

        try {
           resource = uploadFileService.cargar(nameFoto);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            logger.error("Error al formar la URL para la cargar la imagen");
        }
        HttpHeaders cabecera = new HttpHeaders();
        //Cabecera para forzar la descarga
        cabecera.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"");

        return new ResponseEntity<Resource>(resource, cabecera, HttpStatus.OK);
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/clientes/regiones")
    public List<Region> listarRegiones() {
        return clienteService.findByRegiones();
    }
}
