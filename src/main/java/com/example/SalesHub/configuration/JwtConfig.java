package com.example.SalesHub.configuration;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.SalesHub.model.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.time.Instant;

@Component
public class JwtConfig {

    @Value("${secure.key}")
    private String SECRET;

    public String gerar(Usuario usuario) {
        return JWT.create()
                .withIssuer("SALES-API")
                .withSubject(usuario.getNome())
                .withClaim("email", usuario.getEmail())
                .withExpiresAt(Instant.now().plusSeconds(300))
                .withIssuedAt(Instant.now())
                .sign(Algorithm.HMAC256(SECRET));
    }

    public String decodificar(String jwt) {
        return JWT.require(Algorithm.HMAC256(SECRET))
                .withIssuer("SALES-API")
                .build()
                .verify(jwt)
                .getSubject();
    }
}