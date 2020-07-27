package com.cursoejemplo.backend.apirest.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.Arrays;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    @Qualifier("authenticationManager")
    private AuthenticationManager authenticationManager;

    @Autowired
    private InfoAdicionalToken infoAdicionalToken;

    @Override//Es un configurardor de la autorizacion de rutas finales del servidor
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        //Agregar la informacion adicional al token de la clase InfoAdicionalToken
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(infoAdicionalToken, accessTokenConverter()));
        endpoints.authenticationManager(authenticationManager).
                //Se encarga de traducir el token y validar las firmas del mismo.
                accessTokenConverter(accessTokenConverter()).
                tokenEnhancer(tokenEnhancerChain);
    }

    @Bean//Permite la traduccion, decodificacion y codificacion de los datos
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        //Permite personalizar la llave secreta del token al tipo rsa que es mas segura.
        jwtAccessTokenConverter.setSigningKey(OpenSsl.RSA_PRIVADA);
        jwtAccessTokenConverter.setVerifierKey(OpenSsl.RSA_PUBLICA);
        return jwtAccessTokenConverter;
    }

    @Override//Configuracion de los permisos de autorizacion de nuestras rutas de acceso.
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.tokenKeyAccess("permitAll()").
                checkTokenAccess("isAuthenticated()");
    }

    @Override //Configurar los clientes por ej el de angular
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory().withClient("angularapp").//En memoria el cliente ""
                secret(passwordEncoder.encode("12345")).//Clave secreta encriptada
                scopes("read", "write").//Alcance
                authorizedGrantTypes("password", "refresh_token"). //Tipo de autenticacion
                accessTokenValiditySeconds(3600).//Tiempo de validacion del token
                refreshTokenValiditySeconds(3600);//Tiempo de refresh token
    }

}
