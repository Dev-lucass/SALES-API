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
public class Historico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Produto produto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Estoque estoque;

    @Column(nullable = false)
    private BigDecimal quantidadeRetirada;

    @Column(nullable = false)
    private LocalDateTime criadoEm;

    @PrePersist
    private void prePersistir() {
        if (criadoEm == null) criadoEm = LocalDateTime.now();
        if (quantidadeRetirada == null) quantidadeRetirada = BigDecimal.ZERO;
    }
}