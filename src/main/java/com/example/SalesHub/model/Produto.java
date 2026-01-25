package com.example.SalesHub.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,length = 250)
    private String nome;

    @Column(nullable = false,length = 300)
    private String descricao;

    @Column(nullable = false)
    private BigDecimal preco;

    @Column(nullable = false)
    private Boolean ativo;

    @Column(nullable = false)
    private LocalDateTime criadoEm;

    @PrePersist
    private void prePersistir(){
        if(preco == null) preco = BigDecimal.ZERO;
        if(ativo == null) ativo = true;
        if(criadoEm == null) criadoEm = LocalDateTime.now();
    }
}
