package com.cursoejemplo.backend.apirest.model.dao;

import com.cursoejemplo.backend.apirest.model.entity.Producto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface IProductoDao extends PagingAndSortingRepository<Producto, Long> {

    List<Producto>findByNombreContainingIgnoreCase(String nombre);

    @Query("select p from Producto p where p.categoria=?1")
    List<Producto> buscarPorCategoria(String categoria);
}
