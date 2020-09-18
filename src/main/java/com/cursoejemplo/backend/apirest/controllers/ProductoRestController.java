package com.cursoejemplo.backend.apirest.controllers;

import com.cursoejemplo.backend.apirest.model.entity.Producto;
import com.cursoejemplo.backend.apirest.model.services.IProductoService;
import com.cursoejemplo.backend.apirest.model.services.IUploadFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Pageable;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(origins = {"http://localhost:4200", "*"})
@RequestMapping("/api")
public class ProductoRestController {

    @Autowired
    private IProductoService productoService;

    @Autowired
    private IUploadFileService uploadFileService;

    private Logger logger = LoggerFactory.getLogger(ClienteRestController.class);

    private Map<String, Object> map = new HashMap<>();


    @GetMapping("/productos/pagina")
    public ResponseEntity<?> listarPage(Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(productoService.findAll(pageable));
    }

    @GetMapping("/productos")
    public ResponseEntity<?> listar() {
        return ResponseEntity.status(HttpStatus.OK).body(productoService.findAll());
    }

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @GetMapping("/productos/filtar-categoria/{nombre}")
    public ResponseEntity<?> listarPorCategorias(@PathVariable String nombre) {
        return ResponseEntity.status(HttpStatus.OK).body(productoService.findAllCategorias(nombre));
    }

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @GetMapping("/productos/buscar/{id}")
    public ResponseEntity<?> find(@PathVariable Long id) {
        Optional<Producto> productoOptional = productoService.findById(id);

        if (!productoOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(productoOptional.get());
    }

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @PostMapping("/productos")
    public ResponseEntity<?> save(@Valid @RequestBody Producto producto, BindingResult bindingResult) {

        Producto productoActual;

        if (bindingResult.hasErrors()) {
            return validarCampos(bindingResult);
        }

        try {
            productoActual = productoService.saveProducto(producto);
        } catch (DataAccessException e){
            map.put("mensaje", "Error al realizar el insert en la base de datos");
            map.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(map, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(productoActual);

    }

    @Secured("ROLE_ADMIN")
    @PutMapping("/productos/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody Producto producto, BindingResult bindingResult, @PathVariable Long id) {

        Optional<Producto> productoOptional = productoService.findById(id);

        if (!productoOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Producto productoActual = productoOptional.get();

        if (bindingResult.hasErrors()) {
            return validarCampos(bindingResult);
        }

        try {
            productoActual.setNombre(producto.getNombre());
            productoActual.setPrecio(producto.getPrecio());
            productoActual.setCantidad(producto.getCantidad());
            productoActual.setCategoria(producto.getCategoria());
        } catch (DataAccessException e){
            map.put("mensaje", "Error al realizar el insert en la base de datos");
            map.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(map, HttpStatus.INTERNAL_SERVER_ERROR);
        }


        return ResponseEntity.status(HttpStatus.CREATED).body(productoService.saveProducto(productoActual));
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping("/productos/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<Producto> productoOptional = productoService.findById(id);
        if (!productoOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Producto producto = productoOptional.get();
        String nameFileOld = producto.getFoto();
        uploadFileService.eliminarFile(nameFileOld);
        productoService.deleteProducto(id);
        return ResponseEntity.noContent().build();
    }


    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @PostMapping("/productos/upload")
    public ResponseEntity<?> upload(@RequestParam("archivo") MultipartFile archivo, @RequestParam("id") Long id) {

        Map<String, Object> map = new HashMap<String, Object>();
        Optional<Producto> productoOptional = productoService.findById(id);

        if (!productoOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Producto producto = productoOptional.get();

        if(!archivo.isEmpty()) {
            String fileName = null;
            try {
                fileName = uploadFileService.copiarFile(archivo);
            } catch (IOException e) {
                map.put("mensaje", "Error al subir la imagen del cliente ");
                map.put("error", e.getMessage().concat(": ").concat(e.getCause().getMessage()));
                return new ResponseEntity<Map<String, Object>>(map, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            String nameFileOld = producto.getFoto();
            //Si borramos una foto que se borre del directorio uploads
            uploadFileService.eliminarFile(nameFileOld);
            producto.setFoto(fileName);
            productoService.saveProducto(producto);

            map.put("producto", producto);
            map.put("mensaje", "Ha subido correctamente la imagen: " + fileName);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(map);
    }

    @GetMapping("/uploads/imagen/{nameFoto:.+}")
    public ResponseEntity<Resource> verFoto(@PathVariable String nameFoto) {
        Resource resource = null;

        try {
            resource = uploadFileService.cargar(nameFoto);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            logger.error("Error al formar la URL para la cargar la imagen");
        }
        HttpHeaders cabecera = new HttpHeaders();
        cabecera.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"");

        return new ResponseEntity<Resource>(resource, cabecera, HttpStatus.OK);
    }

    private ResponseEntity<?> validarCampos(BindingResult bindingResult) {

        bindingResult.getFieldErrors().forEach( error -> {
            map.put(error.getField(), "El campo: " + error.getField() + " " + error.getDefaultMessage());
        });

        return ResponseEntity.badRequest().body(map);
    }

}
