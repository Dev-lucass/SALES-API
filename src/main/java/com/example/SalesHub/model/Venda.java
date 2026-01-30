package com.example.SalesHub.model;

import com.example.SalesHub.model.enums.StatusPagamento;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Venda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario",nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto", nullable = false)
    private Produto produto;

    @Column(nullable = false)
    private BigDecimal valor;

    @Column(nullable = false)
    private Long quantidade;

    @Column(nullable = false)
    private BigDecimal valorTotalVendas;

    @Column(nullable = false)
    private LocalDateTime dataVenda;

    @PrePersist
    private void prePersistir() {
        if (valor == null) valor = BigDecimal.ZERO;
        if (valorTotalVendas == null) valorTotalVendas = valor;
        if (dataVenda == null) dataVenda = LocalDateTime.now();
    }

    public void aplicarDesconto(BigDecimal precoBase, Double percentual) {
        if (percentual == null || percentual <= 0) {
            this.valor = precoBase;
            return;
        }
        var fatorDesconto = BigDecimal.valueOf(1 - (percentual / 100));
        this.valor = precoBase.multiply(fatorDesconto).setScale(2, RoundingMode.HALF_UP);
    }
}