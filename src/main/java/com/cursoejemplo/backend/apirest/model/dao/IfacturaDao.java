package com.cursoejemplo.backend.apirest.model.dao;

import com.cursoejemplo.backend.apirest.model.entity.Factura;
import org.springframework.data.repository.CrudRepository;

public interface IfacturaDao extends CrudRepository<Factura, Long> {
}
