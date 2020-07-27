package com.cursoejemplo.backend.apirest.controllers;

import com.cursoejemplo.backend.apirest.model.entity.Producto;
import com.cursoejemplo.backend.apirest.model.services.IProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(origins = {"http://localhost:4200", "*"})
@RequestMapping("/api")
public class ProductoRestController {

    @Autowired
    private IProductoService productoService;

    @GetMapping("/productos/page/{page}")
    public ResponseEntity<?> listarPage(@PathVariable Integer page) {
        return ResponseEntity.status(HttpStatus.OK).body(productoService.findAll(PageRequest.of(page, 4)));
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

        if (bindingResult.hasErrors()) {
            return validarCampos(bindingResult);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(productoService.saveProducto(producto));

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

        productoActual.setNombre(producto.getNombre());
        productoActual.setPrecio(producto.getPrecio());
        productoActual.setCantidad(producto.getCantidad());
        productoActual.setCategoria(producto.getCategoria());

        return ResponseEntity.status(HttpStatus.CREATED).body(productoService.saveProducto(productoActual));
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping("/productos/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        productoService.deleteProducto(id);
        return ResponseEntity.noContent().build();
    }

    private ResponseEntity<?> validarCampos(BindingResult bindingResult) {
        Map<String, Object> map = new HashMap<String, Object>();

        bindingResult.getFieldErrors().forEach( error -> {
            map.put(error.getField(), "El campo: " + error.getField() + " " + error.getDefaultMessage());
        });

        return ResponseEntity.badRequest().body(map);
    }

}
