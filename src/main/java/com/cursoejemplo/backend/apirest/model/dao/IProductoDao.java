package com.cursoejemplo.backend.apirest.model.dao;

import com.cursoejemplo.backend.apirest.model.entity.Producto;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface IProductoDao extends PagingAndSortingRepository<Producto, Long> {

    List<Producto>findByNombreContainingIgnoreCase(String nombre);

    List<Producto>findByCategoriaContainingIgnoreCase(String nombre);

}
