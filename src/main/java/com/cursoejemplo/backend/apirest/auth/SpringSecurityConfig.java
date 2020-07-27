package com.cursoejemplo.backend.apirest.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableGlobalMethodSecurity(securedEnabled = true)
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService usuarioService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    @Autowired //Permite registrar y configurar un UserDetailService
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //El passwordEncoder permite encriptar la contraseña para mayor seguridad
       auth.userDetailsService(this.usuarioService).passwordEncoder(passwordEncoder());
    }

    @Override
    @Bean//Permite retornar un authenticationManager para usarlo en el resto de la aplicacion.
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        //Desactivamos el csrf y por defecto cualquier ruta requiere autenticacion
        //No manejamos sesiones por lo tanto establecemos session en stateless (sin estado)
        http.authorizeRequests().anyRequest().authenticated().and().csrf().disable().
                sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
}
