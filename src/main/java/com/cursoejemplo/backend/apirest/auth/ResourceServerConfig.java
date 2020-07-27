package com.cursoejemplo.backend.apirest.auth;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    //Se encargar de dar acceso a los clientes a los recursos de nuestra aplicacion,
    //Siempre y cuando el token que se envie en las cabeceras sea valido.

    @Override//Reglas de seguridad para los endPoints o rutas de acceso a los recursos
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers(HttpMethod.GET,"/api/clientes/page/**",
                "/api/uploads/img/**", "/images/**", "/api/productos/page/**").permitAll() .anyRequest().authenticated().
                and().cors().configurationSource(corsConfigurationSource());
    }



    @Bean//Permite configurar el cors para la comunicacion con Angular
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        //Hacemos referenciar al cliente de angular y indicamos los metodos permitidos
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200", "*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        //Se registra la configuracion para todas las rutas
        source.registerCorsConfiguration("/**", configuration);
        return  source;
    }

    @Bean//Permite registrar un filtro para el Cors
    public FilterRegistrationBean<CorsFilter> corsFilterFilterRegistrationBean() {
        FilterRegistrationBean<CorsFilter> bean =
                new FilterRegistrationBean<CorsFilter>(new CorsFilter(corsConfigurationSource()));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);//Mientras mas bajo el orden mayor es la prioridad
        return bean;
    }
}
