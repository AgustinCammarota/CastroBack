package com.cursoejemplo.backend.apirest.model.services;

import com.cursoejemplo.backend.apirest.model.entity.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IProductoService {

    Page<Producto> findAll(Pageable pageable);

    List<Producto> findAllCategorias(String categoria);

    List<Producto> findAll();

    Optional<Producto> findById(Long id);

    Producto saveProducto(Producto producto);

    void deleteProducto(Long id);

}
