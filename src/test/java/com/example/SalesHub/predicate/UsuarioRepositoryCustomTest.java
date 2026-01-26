package com.example.SalesHub.predicate;

import com.example.SalesHub.configuration.JpaQueryFactoryConfig;
import com.example.SalesHub.dto.filter.UsuarioFilter;
import com.example.SalesHub.model.Usuario;
import com.example.SalesHub.repository.customImpl.UsuarioRepositoryCustom;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;

@DataJpaTest
@Import({UsuarioRepositoryCustom.class, JpaQueryFactoryConfig.class})
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class UsuarioRepositoryCustomTest {

    @Autowired
    private UsuarioRepositoryCustom repositoryCustom;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    void setup() {
        var usuario = Usuario.builder()
                .nome("Admin")
                .email("admin@email.com")
                .senha("123")
                .ativo(true)
                .criadoEm(LocalDateTime.now())
                .build();
        entityManager.persist(usuario);
    }

    @Test
    void deve_buscar_usuario_duplicado_por_email() {
        var filtro = Usuario.builder()
                .nome("Outro")
                .email("admin@email.com")
                .build();

        var resultado = repositoryCustom.buscarUsuarioDuplicado(filtro);

        Assertions.assertThat(resultado).isPresent();
        Assertions.assertThat(resultado.get().getEmail()).isEqualTo("admin@email.com");
    }

    @Test
    void deve_buscar_usuarios_por_filtro_de_nome() {
        var filter = UsuarioFilter.builder()
                .nome("Adm")
                .build();
        var pageable = PageRequest.of(0, 10);

        var resultado = repositoryCustom.buscarUsuarios(filter, pageable);

        Assertions.assertThat(resultado.getContent()).hasSize(1);
        Assertions.assertThat(resultado.getContent().get(0).nome()).isEqualTo("Admin");
    }

    @Test
    void deve_buscar_usuarios_por_intervalo_de_datas() {
        var hoje = LocalDate.now();
        var filter = UsuarioFilter.builder()
                .dataInicial(hoje)
                .dataFinal(hoje)
                .build();
        var pageable = PageRequest.of(0, 10);

        var resultado = repositoryCustom.buscarUsuarios(filter, pageable);

        Assertions.assertThat(resultado.getContent()).isNotEmpty();
    }

    @Test
    void deve_buscar_usuario_existente_por_id() {
        var usuario = Usuario.builder()
                .nome("Teste ID")
                .email("testeid@email.com")
                .senha("333")
                .build();
        entityManager.persist(usuario);

        var id = usuario.getId();

        var resultado = repositoryCustom.buscarUsuarioExistente(id);

        Assertions.assertThat(resultado).isPresent();
        Assertions.assertThat(resultado.get().getNome()).isEqualTo("Teste ID");
    }

    @Test
    void deve_retornar_vazio_quando_usuario_nao_existir() {
        var resultado = repositoryCustom.buscarUsuarioExistente(999L);
        Assertions.assertThat(resultado).isEmpty();
    }
}