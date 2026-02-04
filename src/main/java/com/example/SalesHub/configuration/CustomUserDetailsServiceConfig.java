package com.example.SalesHub.configuration;

import com.example.SalesHub.service.UsuarioService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public record CustomUserDetailsServiceConfig(UsuarioService service) implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String nome) throws UsernameNotFoundException {
        var usuario = service.buscarPeloNome(nome);
        return new CustomUserDetailsConfig(usuario);
    }
}
