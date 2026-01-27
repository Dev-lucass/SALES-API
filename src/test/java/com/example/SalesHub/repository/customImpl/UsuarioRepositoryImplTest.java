package com.example.SalesHub.repository.customImpl;

import com.example.SalesHub.configuration.JpaQueryFactoryConfig;
import com.example.SalesHub.dto.filter.UsuarioFilter;
import com.example.SalesHub.model.Usuario;
import jakarta.persistence.EntityManager;
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
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({UsuarioRepositoryImpl.class, JpaQueryFactoryConfig.class})
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class UsuarioRepositoryImplTest {

    @Autowired
    private UsuarioRepositoryImpl repositoryCustom;

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
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void deve_buscar_usuario_duplicado_por_email() {
        var filtro = Usuario.builder()
                .nome("Outro")
                .email("admin@email.com")
                .build();

        var resultado = repositoryCustom.buscarUsuarioDuplicado(filtro);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getEmail()).isEqualTo("admin@email.com");
    }

    @Test
    void deve_buscar_usuarios_por_filtro_de_nome() {
        var filter = UsuarioFilter.builder()
                .nome("Adm")
                .build();

        var pageable = PageRequest.of(0, 10);

        var resultado = repositoryCustom.buscarUsuarios(filter, pageable);

        assertThat(resultado.getContent()).hasSize(1);
        assertThat(resultado.getContent().get(0).nome()).isEqualTo("Admin");
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

        assertThat(resultado.getContent()).isNotEmpty();
    }

    @Test
    void deve_buscar_usuario_existente_por_id() {
        var usuario = Usuario.builder()
                .nome("Teste ID")
                .email("testeid@email.com")
                .senha("333")
                .ativo(true)
                .build();

        entityManager.persist(usuario);
        entityManager.flush();
        entityManager.clear();

        var resultado = repositoryCustom.buscarUsuarioExistente(usuario.getId());

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getNome()).isEqualTo("Teste ID");
    }

    @Test
    void deve_retornar_vazio_quando_usuario_nao_existir() {
        var resultado = repositoryCustom.buscarUsuarioExistente(999L);
        assertThat(resultado).isEmpty();
    }
}