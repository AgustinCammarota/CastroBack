package com.cursoejemplo.backend.apirest.model.dao;

import com.cursoejemplo.backend.apirest.model.entity.Usuario;
import org.springframework.data.repository.CrudRepository;

public interface IUsuarioDao extends CrudRepository<Usuario, Long> {

    Usuario findByUsername(String username);
}
