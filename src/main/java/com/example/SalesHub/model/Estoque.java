    package com.example.SalesHub.model;

    import jakarta.persistence.*;
    import lombok.AllArgsConstructor;
    import lombok.Builder;
    import lombok.Data;
    import lombok.NoArgsConstructor;

    @Entity
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public class Estoque {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "produto", nullable = false)
        private Produto produto;

        @Column(nullable = false)
        private Long quantidadeInicial;

        @Column(nullable = false)
        private Long quantidadeAtual;

        @Column(nullable = false)
        private Boolean ativo;

        @PrePersist
        private void prePersistir(){
            if(ativo == null) ativo = true;
        }
    }
