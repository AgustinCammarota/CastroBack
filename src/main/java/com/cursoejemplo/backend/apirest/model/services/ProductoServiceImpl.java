package com.cursoejemplo.backend.apirest.model.services;

import com.cursoejemplo.backend.apirest.model.dao.IProductoDao;
import com.cursoejemplo.backend.apirest.model.entity.Producto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoServiceImpl implements IProductoService {

    @Autowired
    private IProductoDao productoDao;

    @Transactional(readOnly = true)
    @Override
    public Page<Producto> findAll(Pageable pageable) {
        return productoDao.findAll(pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Producto> findAllCategorias(String categoria) {
        return productoDao.findByCategoriaContainingIgnoreCase(categoria);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Producto> findAll() {
        return (List<Producto>) productoDao.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Producto> findById(Long id) {
        return productoDao.findById(id);
    }

    @Transactional
    @Override
    public Producto saveProducto(Producto producto) {
        return productoDao.save(producto);
    }

    @Transactional
    @Override
    public void deleteProducto(Long id) {
        productoDao.deleteById(id);
    }
}
