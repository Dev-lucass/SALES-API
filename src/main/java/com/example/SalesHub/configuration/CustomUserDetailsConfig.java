package com.example.SalesHub.configuration;

import com.example.SalesHub.model.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;

public record CustomUserDetailsConfig(Usuario usuario) implements UserDetails {

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(usuario.getFuncao().toString()));
    }

    @Override
    public String getPassword() {
        return usuario().getSenha();
    }

    @Override
    public String getUsername() {
        return usuario().getNome();
    }
}
