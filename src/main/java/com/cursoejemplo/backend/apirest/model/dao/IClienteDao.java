package com.cursoejemplo.backend.apirest.model.dao;

import com.cursoejemplo.backend.apirest.model.entity.Cliente;
import com.cursoejemplo.backend.apirest.model.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IClienteDao extends JpaRepository<Cliente, Long> {

    @Query("from Region")
    List<Region> findAllRegiones();

}
