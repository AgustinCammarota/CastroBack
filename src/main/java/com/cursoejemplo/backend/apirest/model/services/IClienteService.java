package com.cursoejemplo.backend.apirest.model.services;

import com.cursoejemplo.backend.apirest.model.entity.Cliente;
import com.cursoejemplo.backend.apirest.model.entity.Factura;
import com.cursoejemplo.backend.apirest.model.entity.Producto;
import com.cursoejemplo.backend.apirest.model.entity.Region;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IClienteService {

    List<Cliente> findAll();

    Page<Cliente> findAll(Pageable pageable);

    Cliente save (Cliente cliente);

    void delete (Long id);

    Cliente findById(Long id);

    List<Region> findByRegiones();

    Factura findFacturaById(Long id);

    Factura saveFactura(Factura factura);

    void deleteFacturaById(Long id);

    List<Producto> findProductoByNombre(String nombre);

}
