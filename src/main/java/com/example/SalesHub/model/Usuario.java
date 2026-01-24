package com.example.SalesHub.model;

import com.example.SalesHub.model.enums.Funcao;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 150)
    private String nome;

    @Column(nullable = false, unique = true, length = 250)
    private String email;

    @Column(nullable = false, unique = true, length = 250)
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Funcao funcao;

    @Column(nullable = false)
    private Boolean ativo;

    @Column(nullable = false)
    private LocalDateTime criadoEm;

    @PrePersist
    private void prePersistir(){
        if(ativo == null) ativo = true;
        if(funcao == null) funcao = Funcao.CLIENTE;
        if(criadoEm == null) criadoEm = LocalDateTime.now();
    }
}
