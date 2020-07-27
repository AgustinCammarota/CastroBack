package com.cursoejemplo.backend.apirest.model.services;

import com.cursoejemplo.backend.apirest.model.entity.Usuario;

public interface IUsuarioService {

    Usuario findByUsername(String username);

}
