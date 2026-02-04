package com.example.SalesHub.configuration;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class FilterJwtConfig extends OncePerRequestFilter {

    private final JwtConfig jwtConfig;
    private final UserDetailsService detailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        var jwt = recuperarToken(request);

        if (jwt != null) {

            var subject = jwtConfig.decodificar(jwt);
            var usuario = detailsService.loadUserByUsername(subject);

            var autenticacao = new UsernamePasswordAuthenticationToken(
                    usuario,
                    null,
                    usuario.getAuthorities()
            );

            SecurityContextHolder.getContext().setAuthentication(autenticacao);
        }


        filterChain.doFilter(request, response);
    }

    private String recuperarToken(HttpServletRequest request) {

        var authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.replace("Bearer ", "");
        }

        return null;
    }
}
